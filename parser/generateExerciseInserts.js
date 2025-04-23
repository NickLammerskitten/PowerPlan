const fs = require('fs');

function escapeSQL(str) {
    if (str === null || str === undefined || str === 'NULL') return 'NULL';
    return `'${str.replace(/'/g, "''")}'`;
}

function handleUnsorted(value) {
    if (value === null || value === undefined) {
        return null;
    }
    return `'${value}'`;
}

function handleEnum(value) {
    if (value === null || value === undefined) return null;
    return `${value}`;
}

const data = JSON.parse(fs.readFileSync('parsed_exercises.json', 'utf8'));

let sqlContent = `-- PostgreSQL Syntax for Exercise Import
BEGIN;

-- Equipment Inserts\n`;

// Create a set to track all unique equipment
const equipmentSet = new Set();

// Collect all equipment from the data
data.forEach(item => {
    if (item.exercise.primary_equipment) {
        equipmentSet.add(item.exercise.primary_equipment);
    }
    if (item.exercise.secondary_equipment) {
        equipmentSet.add(item.exercise.secondary_equipment);
    }
});

// Insert equipment
Array.from(equipmentSet).forEach(equipment => {
    sqlContent += `
        INSERT INTO equipments (name)
        VALUES (${escapeSQL(equipment)})
    `;
});

sqlContent += `\n-- Exercise Inserts\n`;

data.forEach((item, index) => {
    const exercise = item.exercise;

    const tempVarName = `temp_exercise_${index}`;

    const bodySection = handleUnsorted(exercise.body_section);
    const classification = handleUnsorted(exercise.classification);
    const difficultyLevel = handleEnum(exercise.difficulty_level) ?? 'BEGINNER';

    sqlContent += `
-- Exercise: ${exercise.name}
WITH ${tempVarName} AS (
  INSERT INTO exercises (
    name, 
    short_video_url, 
    long_video_url, 
    difficulty_level, 
    primary_equipment_id, 
    secondary_equipment_id, 
    body_section, 
    classification
  ) 
  SELECT 
    ${escapeSQL(exercise.name)},
    ${escapeSQL(exercise.short_video_url)},
    ${escapeSQL(exercise.long_video_url)},
    '${difficultyLevel}',
    e1.id,
    e2.id,
    ${escapeSQL(exercise.body_section)},
    ${escapeSQL(exercise.classification)}
  FROM 
    (SELECT id FROM equipments WHERE name = ${escapeSQL(exercise.primary_equipment)}) e1
    LEFT JOIN (SELECT id FROM equipments WHERE name = ${escapeSQL(exercise.secondary_equipment)}) e2 ON true
  RETURNING id
)`;

    if (item.muscles && item.muscles.length > 0) {
        const uniqueMuscles = new Map();

        item.muscles.forEach(muscle => {
            const key = muscle.group;
            if (!uniqueMuscles.has(key) || muscle.intensity === 'PRIMARY' || muscle.role === 'PRIMARY') {
                uniqueMuscles.set(key, muscle);
            }
        });

        const muscleValues = Array.from(uniqueMuscles.values())
            .map(muscle => {
                return `((SELECT id FROM ${tempVarName}), '${muscle.group}', '${muscle.intensity}', '${muscle.role}')`;
            })
            .join(',\n    ');

        sqlContent += `
            INSERT INTO exercises_muscles (exercise_id,
                                           muscle_group,
                                           intensity,
                                           muscle_role)
            VALUES
                ${muscleValues};
        `;
    }
});

sqlContent += `\nCOMMIT;`;

fs.writeFileSync('exercise_import.sql', sqlContent);
console.log('PostgreSQL SQL file has been generated: exercise_import.sql');