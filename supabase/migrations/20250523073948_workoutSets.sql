create table public.workout_sets
(
    id               uuid not null
        constraint workout_sets_pk
            primary key,
    set_id           uuid not null
        constraint workout_sets_plans_sets_id_fk
            references public.plans_sets,
    weight           numeric,
    reps             integer,
    duration_seconds integer
);

alter table public.workout_sets
    owner to postgres;

grant delete, insert, references, select, trigger, truncate, update on public.workout_sets to anon;

grant delete, insert, references, select, trigger, truncate, update on public.workout_sets to authenticated;

grant delete, insert, references, select, trigger, truncate, update on public.workout_sets to service_role;

