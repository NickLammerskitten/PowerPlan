INSERT INTO public.plans (id, name, difficulty_level, classifications, is_template, user_id)
VALUES ('c811473f-947d-43b2-b637-b8852141991d', 'Test Plan', 'BEGINNER', '{POWERLIFTING,BODYBUILDING}', true,
        '596eedbb-6c17-47b6-9878-2959c11b6851');

INSERT INTO public.plans_weeks (plan_id, index, id, user_id)
VALUES ('c811473f-947d-43b2-b637-b8852141991d', '1', 'ff5160c3-1ecd-42fe-9c42-4146e76a9693',
        '596eedbb-6c17-47b6-9878-2959c11b6851');

INSERT INTO public.plans_training_days (id, plans_weeks_id, index, name, type, user_id)
VALUES ('ff2519a7-018f-4108-98d0-e69883187629', 'ff5160c3-1ecd-42fe-9c42-4146e76a9693', '-1', 'Day 1',
        'STRENGTH_TRAINING', '596eedbb-6c17-47b6-9878-2959c11b6851');

INSERT INTO public.plans_exercises (id, index, exercise_id, plans_training_days_id, user_id)
VALUES ('ff574f43-4b5b-4946-a2b2-8b070b67108f', '-1', 'f4f6566b-d678-4b43-87a0-4bdaa18a0778',
        'ff2519a7-018f-4108-98d0-e69883187629', '596eedbb-6c17-47b6-9878-2959c11b6851');

INSERT INTO public.plans_sets (id, index, plans_exercises_id, repetitions_scheme, fixed_reps, max_reps, min_reps,
                               goal_scheme, rpe, min_rpe, max_rpe, percent_one_rep_max, user_id)
VALUES ('721d56b4-a461-4ee3-885f-6ca9497eff54', '-1', 'ff574f43-4b5b-4946-a2b2-8b070b67108f', 'FIXED', 10, null, null,
        'RPE', 4, null, null, null, '596eedbb-6c17-47b6-9878-2959c11b6851');
