alter table "public"."plans"
    add column "user_id" uuid not null default auth.uid();

alter table "public"."plans"
    enable row level security;

create policy "Enable operations only with user id"
    on "public"."plans"
    as permissive
    for all
    to authenticated
    using (((SELECT auth.uid() AS uid) = user_id));

alter table "public"."plans_exercises"
    add column "user_id" uuid not null default auth.uid();

alter table "public"."plans_exercises"
    enable row level security;

create policy "Enable operations only with user id"
    on "public"."plans_exercises"
    as permissive
    for all
    to authenticated
    using (((SELECT auth.uid() AS uid) = user_id));

alter table "public"."plans_sets"
    add column "user_id" uuid not null default auth.uid();

alter table "public"."plans_sets"
    enable row level security;

create policy "Enable operations only with user id"
    on "public"."plans_sets"
    as permissive
    for all
    to authenticated
    using (((SELECT auth.uid() AS uid) = user_id));

alter table "public"."plans_training_days"
    add column "user_id" uuid not null default auth.uid();

alter table "public"."plans_training_days"
    enable row level security;

create policy "Enable operations only with user id"
    on "public"."plans_training_days"
    as permissive
    for all
    to authenticated
    using (((SELECT auth.uid() AS uid) = user_id));

alter table "public"."plans_weeks"
    add column "user_id" uuid not null default auth.uid();

alter table "public"."plans_weeks"
    enable row level security;

create policy "Enable operations only with user id"
    on "public"."plans_weeks"
    as permissive
    for all
    to authenticated
    using (((SELECT auth.uid() AS uid) = user_id));

alter table "public"."workout_sessions"
    add column "user_id" uuid not null default auth.uid();

alter table "public"."workout_sessions"
    enable row level security;

create policy "Enable operations only with user id"
    on "public"."workout_sessions"
    as permissive
    for all
    to authenticated
    using (((SELECT auth.uid() AS uid) = user_id));

alter table "public"."workout_sets"
    add column "user_id" uuid not null default auth.uid();

alter table "public"."workout_sets"
    enable row level security;

create policy "Enable operations only with user id"
    on "public"."workout_sets"
    as permissive
    for all
    to authenticated
    using (((SELECT auth.uid() AS uid) = user_id));
