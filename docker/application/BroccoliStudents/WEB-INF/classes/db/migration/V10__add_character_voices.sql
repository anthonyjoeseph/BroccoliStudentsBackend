use broccoli_students;

create table CHARACTER_VOICES(
  NAME varchar(100) not null,
  CHARACTER_VOICE_ID bigint not null AUTO_INCREMENT,
  
  primary key (CHARACTER_VOICE_ID)
);

create table BOOK_CHARACTER_VOICES(
  BOOK_ID bigint not null,
  foreign key (BOOK_ID) references BOOKS(BOOK_ID) on delete cascade,
  
  CHARACTER_VOICE_ID bigint not null,
  foreign key (CHARACTER_VOICE_ID ) references CHARACTER_VOICES(CHARACTER_VOICE_ID),
  
  primary key (BOOK_ID, CHARACTER_VOICE_ID)
);

insert into CHARACTER_VOICES (NAME) values ("Squirrel");
insert into CHARACTER_VOICES (NAME) values ("Vader");
insert into CHARACTER_VOICES (NAME) values ("Alien");

insert into BOOK_CHARACTER_VOICES (BOOK_ID, CHARACTER_VOICE_ID) values (
  (select BOOK_ID from BOOKS where TITLE="Little Frog"),
  (select CHARACTER_VOICE_ID from CHARACTER_VOICES where NAME="Squirrel")
);
insert into BOOK_CHARACTER_VOICES (BOOK_ID, CHARACTER_VOICE_ID) values (
  (select BOOK_ID from BOOKS where TITLE="Little Frog"),
  (select CHARACTER_VOICE_ID from CHARACTER_VOICES where NAME="Vader")
);
insert into BOOK_CHARACTER_VOICES (BOOK_ID, CHARACTER_VOICE_ID) values (
  (select BOOK_ID from BOOKS where TITLE="Little Frog"),
  (select CHARACTER_VOICE_ID from CHARACTER_VOICES where NAME="Alien")
);