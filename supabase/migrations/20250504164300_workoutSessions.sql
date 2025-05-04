create type "public"."PlanStatus" as enum ('ACTIVE', 'FINISHED');

alter table "public"."plans" add column "is_template" boolean not null default true;
alter table "public"."plans" add column "status" "PlanStatus";