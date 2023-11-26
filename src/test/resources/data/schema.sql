drop table if exists training;
drop table if exists trainee;
drop table if exists trainer;
drop table if exists training_type;
drop table if exists user_entity;


create table training_type
(
    id   bigserial
        primary key,
    name varchar(255) not null
        unique
);

create table user_entity
(
    date_of_birth date,
    is_active     boolean default false not null,
    id            bigserial
        primary key,
    password      varchar(15)           not null,
    firstname     varchar(50)           not null,
    lastname      varchar(50)           not null,
    address       varchar(255),
    username      varchar(255)          not null
        unique
);

create table trainee
(
    id             bigserial
        primary key,
    user_entity_id bigint
        unique
        constraint fkqji4u13nf39iwnl3eth7904hg
            references user_entity
);

create table trainer
(
    id                bigserial
        primary key,
    specialization_id bigint
        unique
        constraint fk8kfknuu0rsxcbd8gykyw2ag65
            references training_type,
    user_entity_id    bigint
        unique
        constraint fksj6dt27mjm0r6gwfqpg4r5cip
            references user_entity
);

create table training
(
    duration      integer      not null,
    id            bigserial
        primary key,
    trainee_id    bigint
        constraint fki2dctw34e0xl50d8tsnrre7te
            references trainee,
    trainer_id    bigint
        constraint fk7r3b25ygw5bdjamojskmpk0b9
            references trainer,
    training_date timestamp(6) not null,
    training_name varchar(255) not null
);
