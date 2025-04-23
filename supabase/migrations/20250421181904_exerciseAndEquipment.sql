-- Setup ENUMS: BodySection, Classification, DifficultyLevel, IntensityLevel, MuscleGroup, MuscleRole
create type public."DifficultyLevel" as enum ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT');
alter type public."DifficultyLevel" owner to postgres;

create type public."MuscleGroup" as enum ('TRAPEZIUS', 'BACK', 'CHEST', 'BICEPS', 'TRICEPS', 'FOREARMS', 'SHOULDERS', 'ABS', 'ABDUCTORS', 'HAMSTRINGS', 'QUADRICEPS', 'ADDUCTORS', 'CALVES', 'GLUTES', 'HIP_FLEXORS', 'SHINS');
alter type public."MuscleGroup" owner to postgres;

create type public."IntensityLevel" as enum ('PRIMARY', 'SECONDARY');
alter type public."IntensityLevel" owner to postgres;

create type public."BodySection" as enum ('UPPER_BODY', 'MIDSECTION', 'LOWER_BODY', 'FULL_BODY', 'UNSORTED');
alter type public."BodySection" owner to postgres;

create type public."Classification" as enum ('POSTURAL', 'BODYBUILDING', 'UNSORTED', 'CALISTHENICS', 'ANIMAL_FLOW', 'GRINDS', 'POWERLIFTING', 'MOBILITY', 'PLYOMETRIC', 'BALLISTICS', 'OLYMPIC_WEIGHTLIFTING', 'BALANCE');
alter type public."Classification" owner to postgres;

create type public."MuscleRole" as enum ('PRIMARY', 'SECONDARY');
alter type public."MuscleRole" owner to postgres;

-- Setup TABLE: Equipment
create table public.equipments
(
    id   uuid default gen_random_uuid() not null
        primary key,
    name text                           not null
);

alter table "public".equipments enable row level security;

alter table public.equipments
    owner to postgres;

create policy "Enable read access for all users" on public.equipments
    as permissive
    for select
    using (true);

grant delete, insert, references, select, trigger, truncate, update on public.equipments to anon;
grant delete, insert, references, select, trigger, truncate, update on public.equipments to authenticated;
grant delete, insert, references, select, trigger, truncate, update on public.equipments to service_role;

-- Setup TABLE: Exercise

create table public.exercises
(
    id                     uuid default gen_random_uuid() not null
        primary key,
    name                   text                           not null,
    short_video_url        text,
    long_video_url         text,
    difficulty_level       "DifficultyLevel"              not null,
    primary_equipment_id   uuid                           not null
        references public.equipments
            on update restrict on delete restrict,
    secondary_equipment_id uuid
        references public.equipments
            on update restrict on delete restrict,
    body_section           "BodySection"                  not null,
    classification         "Classification"               not null
);

alter table "public".exercises enable row level security;

alter table public.exercises
    owner to postgres;

create policy "Enable read access for all users" on public.exercises
    as permissive
    for select
    using (true);

grant delete, insert, references, select, trigger, truncate, update on public.exercises to anon;
grant delete, insert, references, select, trigger, truncate, update on public.exercises to authenticated;
grant delete, insert, references, select, trigger, truncate, update on public.exercises to service_role;

-- Setup TABLE: ExerciseMuscle

create table public.exercises_muscles
(
    exercise_id  uuid             not null
        references public.exercises
            on update cascade on delete cascade,
    muscle_group "MuscleGroup"    not null,
    intensity    "IntensityLevel" not null,
    role  "MuscleRole"     not null,
    primary key (exercise_id, muscle_group)
);

alter table "public".exercises_muscles enable row level security;

alter table public.exercises_muscles
    owner to postgres;

create policy "Enable read access for all users" on public.exercises_muscles
    as permissive
    for select
    using (true);

grant delete, insert, references, select, trigger, truncate, update on public.exercises_muscles to anon;
grant delete, insert, references, select, trigger, truncate, update on public.exercises_muscles to authenticated;
grant delete, insert, references, select, trigger, truncate, update on public.exercises_muscles to service_role;
