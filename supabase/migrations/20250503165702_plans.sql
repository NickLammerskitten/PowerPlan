create table public.plans
(
    id               uuid not null primary key,
    name             text not null,
    difficulty_level "DifficultyLevel",
    classifications  "Classification"[]                     not null
);

comment on table public.plans is 'Training plans';

alter table public.plans owner to postgres;

grant delete, insert, references, select, trigger, truncate, update on public.plans to anon;

grant delete, insert, references, select, trigger, truncate, update on public.plans to authenticated;

grant delete, insert, references, select, trigger, truncate, update on public.plans to service_role;

create table public.plans_weeks
(
    plan_id uuid not null
        references public.plans
            on update cascade on delete cascade,
    index   text not null,
    id      uuid not null
        primary key,
    constraint plans_weeks_pk
        unique (index, plan_id)
);

alter table public.plans_weeks
    owner to postgres;

grant
delete, insert, references, select, trigger, truncate, update on public.plans_weeks to anon;

grant delete, insert, references, select, trigger, truncate, update on public.plans_weeks to authenticated;

grant delete, insert, references, select, trigger, truncate, update on public.plans_weeks to service_role;

create table public.plans_training_days
(
    id             uuid not null
        primary key,
    plans_weeks_id uuid not null
        constraint plans_training_days_plans_weeks_id_fkey
            references plans_weeks(id) ON UPDATE CASCADE ON DELETE CASCADE,
    index          text not null,
    name           text not null,
    constraint plans_training_days_pk
        unique (index, plans_weeks_id)
);

alter table public.plans_training_days owner to postgres;

grant delete, insert, references, select, trigger, truncate, update on public.plans_training_days to anon;

grant delete, insert, references, select, trigger, truncate, update on public.plans_training_days to authenticated;

grant delete, insert, references, select, trigger, truncate, update on public.plans_training_days to service_role;

create table public.plans_exercises
(
    id                     uuid not null
        primary key,
    index                  text not null,
    exercise_id            uuid not null,
    plans_training_days_id uuid not null
        references public.plans_training_days
            on update cascade on delete cascade,
    constraint plans_exercises_pk
        unique (plans_training_days_id, index)
);

alter table public.plans_exercises
    owner to postgres;

grant delete, insert, references, select, trigger, truncate, update on public.plans_exercises to anon;

grant delete, insert, references, select, trigger, truncate, update on public.plans_exercises to authenticated;

grant delete, insert, references, select, trigger, truncate, update on public.plans_exercises to service_role;

create table public.plans_sets
(
    id                  uuid not null
        constraint plans_sets_pk
            primary key,
    index               text not null,
    plans_exercises_id  uuid not null
        references public.plans_exercises
            on update cascade on delete cascade,
    repetitions_scheme  text not null,
    fixed_reps          smallint,
    max_reps            smallint,
    min_reps            smallint,
    goal_scheme         text not null,
    rpe                 numeric,
    min_rpe             numeric,
    max_rpe             numeric,
    percent_one_rep_max numeric,
    constraint plans_sets_pk_2
        unique (index, plans_exercises_id)
);

comment on column public.plans_sets.repetitions_scheme is 'REPETITIONS SCHEME ENUM';

comment on column public.plans_sets.goal_scheme is 'GOAL SCHEME ENUM';

comment on column public.plans_sets.percent_one_rep_max is 'Percent of the one rep max';

alter table public.plans_sets
    owner to postgres;

grant delete, insert, references, select, trigger, truncate, update on public.plans_sets to anon;

grant delete, insert, references, select, trigger, truncate, update on public.plans_sets to authenticated;

grant delete, insert, references, select, trigger, truncate, update on public.plans_sets to service_role;
