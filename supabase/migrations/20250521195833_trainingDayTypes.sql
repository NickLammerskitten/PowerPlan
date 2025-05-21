create type "public"."TrainingType" as enum ('STRENGTH_TRAINING');

alter table "public"."plans_training_days"
    add column "type" "TrainingType" default 'STRENGTH_TRAINING' NOT NULL;