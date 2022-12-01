CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table books
(
    id     uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    author text not null,
    title  text not null
);

