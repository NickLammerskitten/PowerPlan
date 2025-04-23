const csv = require('csv-parser');
const fs = require('fs');

const results = [];

function normalizeEnum(value, enumType) {
    if (!value) {
        return null;
    }
    const val = value.trim().toLowerCase();

    const map = {
        DifficultyLevel: {
            'beginner': 'BEGINNER',
            'intermediate': 'INTERMEDIATE',
            'advanced': 'ADVANCED',
            'expert': 'EXPERT',
        },
        MuscleGroup: {
            'abdominals': 'ABS',
            'rectus abdominis': 'ABS',
            'obliques': 'ABS',
            'rectus femoris': 'QUADRICEPS',
            'trapezius': 'TRAPEZIUS',
            'back': 'BACK',
            'chest': 'CHEST',
            'biceps': 'BICEPS',
            'triceps': 'TRICEPS',
            'forearms': 'FOREARMS',
            'shoulders': 'SHOULDERS',
            'quadriceps': 'QUADRICEPS',
            'hamstrings': 'HAMSTRINGS',
            'glutes': 'GLUTES',
            'hip flexors': 'HIP_FLEXORS',
            'shins': 'SHINS',
            'adductors': 'ADDUCTORS',
            'abductors': 'ABDUCTORS',
            'calves': 'CALVES',
        },
        BodySection: {
            'midsection': 'MIDSECTION',
            'upper body': 'UPPER_BODY',
            'lower body': 'LOWER_BODY',
            'full body': 'FULL_BODY',
        },
        Classification: {
            'postural': 'POSTURAL',
            'bodybuilding': 'BODYBUILDING',
            'calisthenics': 'CALISTHENICS',
            'animal flow': 'ANIMAL_FLOW',
            'grinds': 'GRINDS',
            'powerlifting': 'POWERLIFTING',
            'mobility': 'MOBILITY',
            'plyometric': 'PLYOMETRIC',
            'ballistics': 'BALLISTICS',
            'olympic weightlifting': 'OLYMPIC_WEIGHTLIFTING',
            'balance': 'BALANCE',
        },
    };

    return map[enumType]?.[val] || null;
}

function cleanKey(key) {
    return key
        .trim()
        .replace(/#/g, '')
        .replace(/\s+/g, '_')
        .replace(/[^\w]/g, '');
}

fs.createReadStream('dataSourceConverted.csv')
    .pipe(csv({ separator: ';' }))
    .on('data', (rawRow) => {
        const row = {};
        for (let key in rawRow) {
            row[cleanKey(key)] = rawRow[key];
        }

        const muscles = [];

        // Muscle mapping: Target = PRIMARY, Prime/Secondary/Tertiary = SECONDARY
        const addMuscle = (val, intensity, role) => {
            const group = normalizeEnum(val, 'MuscleGroup');
            if (group) {
                muscles.push({
                    group,
                    intensity,
                    role,
                });
            }
        };

        addMuscle(row.Target_Muscle_Group, 'PRIMARY', 'PRIMARY');
        addMuscle(row.Prime_Mover_Muscle, 'SECONDARY', 'SECONDARY');
        addMuscle(row.Secondary_Muscle, 'SECONDARY', 'SECONDARY');
        addMuscle(row.Tertiary_Muscle, 'SECONDARY', 'SECONDARY');

        results.push({
            exercise: {
                name: row.Exercise,
                short_video_url: row.Short_YouTube_Demonstration === '0' ? null : row.Short_YouTube_Demonstration || null,
                long_video_url: row.InDepth_YouTube_Explanation === '0' ? null : row.InDepth_YouTube_Explanation || null,
                difficulty_level: normalizeEnum(row.Difficulty_Level, 'DifficultyLevel'),
                primary_equipment: row.Primary_Equipment !== 'None' ? row.Primary_Equipment : null,
                secondary_equipment: row.Secondary_Equipment !== 'None' ? row.Secondary_Equipment : null,
                body_section: normalizeEnum(row.Body_Region, 'BodySection') ?? 'UNSORTED',
                classification: normalizeEnum(row.Primary_Exercise_Classification, 'Classification') ?? 'UNSORTED',
            },
            muscles,
        });
    })
    .on('end', () => {
        fs.writeFileSync('parsed_exercises.json', JSON.stringify(results, null, 2));
        console.log('✅ Exported parsed_exercises.json');
    });
