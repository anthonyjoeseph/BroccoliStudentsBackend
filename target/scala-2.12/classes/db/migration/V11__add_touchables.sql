create table MUSIC(
  URI varchar(500) not null,
  MUSIC_ID bigint not null AUTO_INCREMENT,
  
  primary key (MUSIC_ID)
);

create table BOOK_PAGE_MUSIC(
  BOOK_PAGE_ID bigint not null,
  foreign key (BOOK_PAGE_ID) references BOOK_PAGES(BOOK_PAGE_ID),

  MUSIC_ID bigint not null,
  foreign key (MUSIC_ID) references MUSIC(MUSIC_ID),
  
  unique (BOOK_PAGE_ID),
  primary key (BOOK_PAGE_ID, MUSIC_ID)
);

create table QUIZ_MUSIC(
  QUIZ_ID bigint not null,
  foreign key (QUIZ_ID) references QUIZZES(QUIZ_ID),

  MUSIC_ID bigint not null,
  foreign key (MUSIC_ID) references MUSIC(MUSIC_ID),
  
  unique (QUIZ_ID),
  primary key (QUIZ_ID, MUSIC_ID)
);

create table TOUCH_ACTIONS(
  TYPE varchar(100) not null,
  TOUCH_ACTION_ID int not null AUTO_INCREMENT,
  
  primary key (TOUCH_ACTION_ID)
);

insert into TOUCH_ACTIONS (TYPE) values ("QUIZ");
insert into TOUCH_ACTIONS (TYPE) values ("SOUND");
insert into TOUCH_ACTIONS (TYPE) values ("ANIMATION");
insert into TOUCH_ACTIONS (TYPE) values ("PRONUNCIATION");

create table TOUCHABLES(
  IMAGE_ID bigint not null,
  foreign key (IMAGE_ID) references IMAGES(IMAGE_ID),
  
  TOUCHABLE_ID bigint not null AUTO_INCREMENT,
  primary key (TOUCHABLE_ID)
);

insert into TOUCHABLES (IMAGE_ID) values (
  (select IMAGE_ID from IMAGES where X_PERCENT=0.5 AND URI="frog_outline")
);
insert into TOUCHABLES (IMAGE_ID) values (
  (select IMAGE_ID from IMAGES where X_PERCENT=0.6)
);

alter table QUIZZES add TOUCHABLE_ID bigint;

update QUIZZES 
  set TOUCHABLE_ID=
    (select TOUCHABLE_ID from TOUCHABLES where IMAGE_ID=(select IMAGE_ID from IMAGES where X_PERCENT=0.5 AND URI="frog_outline"))
  where IMAGE_ID=
    (select IMAGE_ID from IMAGES where X_PERCENT=0.5 AND URI="frog_outline");
update QUIZZES 
  set TOUCHABLE_ID=
    (select TOUCHABLE_ID from TOUCHABLES where IMAGE_ID=(select IMAGE_ID from IMAGES where X_PERCENT=0.6))
  where IMAGE_ID=
    (select IMAGE_ID from IMAGES where X_PERCENT=0.6);
  
alter table QUIZZES modify TOUCHABLE_ID bigint not null;
alter table QUIZZES add foreign key (TOUCHABLE_ID) references TOUCHABLES(TOUCHABLE_ID);
alter table QUIZZES drop foreign key QUIZZES_ibfk_2;

create table TOUCHABLE_TOUCH_ACTIONS(
  TOUCHABLE_ID bigint not null,
  foreign key (TOUCHABLE_ID) references TOUCHABLES(TOUCHABLE_ID),

  TOUCH_ACTION_ID int not null,
  foreign key (TOUCH_ACTION_ID) references TOUCH_ACTIONS(TOUCH_ACTION_ID),
  
  primary key (TOUCHABLE_ID, TOUCH_ACTION_ID)
);

create table TOUCHABLE_SOUNDS(
  URI varchar(500) not null,
  
  TOUCHABLE_ID bigint not null,
  foreign key (TOUCHABLE_ID) references TOUCHABLES(TOUCHABLE_ID),
  
  TOUCHABLE_SOUND_ID bigint not null AUTO_INCREMENT,
  primary key (TOUCHABLE_SOUND_ID)
);

create table TOUCHABLE_PRONUNCIATIONS(
  SOUND_URI varchar(500) not null,
  IMAGE_URI varchar(500) not null,
  TEXT varchar(500) not null,
  
  TOUCHABLE_ID bigint not null,
  foreign key (TOUCHABLE_ID) references TOUCHABLES(TOUCHABLE_ID),
  
  TOUCHABLE_PRONUNCIATION_ID bigint not null AUTO_INCREMENT,
  primary key (TOUCHABLE_PRONUNCIATION_ID)
);

create table BOOK_PAGE_IMAGES(
  BOOK_PAGE_ID bigint not null,
  foreign key (BOOK_PAGE_ID) references BOOK_PAGES(BOOK_PAGE_ID),

  IMAGE_ID bigint not null,
  foreign key (IMAGE_ID) references IMAGES(IMAGE_ID),
  
  primary key (BOOK_PAGE_ID, IMAGE_ID)
);

insert into BOOK_PAGE_IMAGES (BOOK_PAGE_ID, IMAGE_ID) values (
  (select BOOK_PAGE_ID from IMAGES where IMAGE_ID=
    (select IMAGE_ID from IMAGES where X_PERCENT=0.5 AND URI="frog_outline")
  ),
  (select IMAGE_ID from IMAGES where IMAGE_ID=
    (select IMAGE_ID from IMAGES where X_PERCENT=0.5 AND URI="frog_outline")
  )
);

insert into BOOK_PAGE_IMAGES (BOOK_PAGE_ID, IMAGE_ID) values (
  (select BOOK_PAGE_ID from IMAGES where IMAGE_ID=
    (select IMAGE_ID from IMAGES where X_PERCENT=0.6)
  ),
  (select IMAGE_ID from IMAGES where IMAGE_ID=
    (select IMAGE_ID from IMAGES where X_PERCENT=0.6)
  )
);

insert into BOOK_PAGE_IMAGES (BOOK_PAGE_ID, IMAGE_ID) values (
  (select BOOK_PAGE_ID from IMAGES where IMAGE_ID=
    (select IMAGE_ID from IMAGES where X_PERCENT=0.5 AND URI="spongebob")
  ),
  (select IMAGE_ID from IMAGES where IMAGE_ID=
    (select IMAGE_ID from IMAGES where X_PERCENT=0.5 AND URI="spongebob")
  )
);
insert into BOOK_PAGE_IMAGES (BOOK_PAGE_ID, IMAGE_ID) values (
  (select BOOK_PAGE_ID from IMAGES where IMAGE_ID=
    (select IMAGE_ID from IMAGES where X_PERCENT=0.29)
  ),
  (select IMAGE_ID from IMAGES where IMAGE_ID=
    (select IMAGE_ID from IMAGES where X_PERCENT=0.29)
  )
);

alter table IMAGES drop foreign key IMAGES_ibfk_1;
alter table IMAGES drop column BOOK_PAGE_ID;

create table TOUCHABLE_ANIMATIONS(
  IMAGE_ID bigint not null,
  foreign key (IMAGE_ID) references IMAGES(IMAGE_ID),
  
  TOUCHABLE_ID bigint not null,
  foreign key (TOUCHABLE_ID) references TOUCHABLES(TOUCHABLE_ID),
  
  TOUCHABLE_ANIMATION_ID bigint not null AUTO_INCREMENT,
  primary key (TOUCHABLE_ANIMATION_ID)
);