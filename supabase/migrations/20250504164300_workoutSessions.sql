create type "public"."PlanStatus" as enum ('ACTIVE', 'FINISHED');

alter table "public"."plans"
    add column "is_template" boolean not null default true;
alter table "public"."plans"
    add column "status" "PlanStatus";

create table public.workout_sessions
(
    id                   uuid               not null
        constraint workout_sessions_pk
            primary key,
    plan_training_day_id uuid               not null
        constraint workout_sessions_plans_training_days_id_fk
            references public.plans_training_days,
    start_time           timestamp default now() not null,
    duration             integer,
    notes                text
);

comment on column public.workout_sessions.duration is 'Seconds';

alter table public.workout_sessions
    owner to postgres;

grant delete, insert, references, select, trigger, truncate, update on public.workout_sessions to anon;
grant delete, insert, references, select, trigger, truncate, update on public.workout_sessions to authenticated;
grant delete, insert, references, select, trigger, truncate, update on public.workout_sessions to service_role;

