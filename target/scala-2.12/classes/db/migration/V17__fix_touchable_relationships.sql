/* Images reference book_pages, drop book_page_images */

alter table IMAGES add BOOK_PAGE_ID BIGINT;
alter table IMAGES add foreign key IMAGE_BOOK_PAGE_REFERENCE (BOOK_PAGE_ID) references BOOK_PAGES(BOOK_PAGE_ID) on delete cascade;

update IMAGES set BOOK_PAGE_ID=1;
alter table IMAGES modify BOOK_PAGE_ID bigint not null;

drop table BOOK_PAGE_IMAGES;

/* quizzes no longer reference touchables */

alter table QUIZZES drop foreign key QUIZZES_ibfk_1;
alter table QUIZZES drop column TOUCHABLE_ID;

/* touchable_sound, touchable_pronunciations,
    touchable_animations reference images not touchables */
    
alter table TOUCHABLE_SOUNDS drop foreign key TOUCHABLE_SOUNDS_ibfk_1;
alter table TOUCHABLE_SOUNDS add column IMAGE_ID bigint;
alter table TOUCHABLE_SOUNDS add foreign key TOUCHABLE_SOUND_IMAGE_REFERENCE (IMAGE_ID) references IMAGES(IMAGE_ID) on delete cascade;
update TOUCHABLE_SOUNDS set IMAGE_ID=1;
alter table TOUCHABLE_SOUNDS modify IMAGE_ID bigint not null;

alter table TOUCHABLE_PRONUNCIATIONS drop foreign key TOUCHABLE_PRONUNCIATIONS_ibfk_1;
alter table TOUCHABLE_PRONUNCIATIONS add column IMAGE_ID bigint;
alter table TOUCHABLE_PRONUNCIATIONS add foreign key TOUCHABLE_PRONUNCIATIONS_IMAGE_REFERENCE (IMAGE_ID) references IMAGES(IMAGE_ID) on delete cascade;
update TOUCHABLE_PRONUNCIATIONS set IMAGE_ID=1;
alter table TOUCHABLE_PRONUNCIATIONS modify IMAGE_ID bigint not null;

alter table TOUCHABLE_ANIMATIONS drop foreign key TOUCHABLE_ANIMATIONS_ibfk_1;
alter table TOUCHABLE_ANIMATIONS change IMAGE_ID RESULTING_IMAGE_ID bigint not null;
alter table TOUCHABLE_ANIMATIONS add foreign key RESULTING_IMAGE_REFERENCE (RESULTING_IMAGE_ID) references IMAGES(IMAGE_ID);
alter table TOUCHABLE_ANIMATIONS add column IMAGE_ID bigint;
alter table TOUCHABLE_ANIMATIONS add foreign key TOUCHABLE_ANIMATION_IMAGE_REFERENCE (IMAGE_ID) references IMAGES(IMAGE_ID) on delete cascade;
update TOUCHABLE_ANIMATIONS set IMAGE_ID=1;
alter table TOUCHABLE_ANIMATIONS modify IMAGE_ID bigint not null;

/* drop table TOUCHABLES; */

drop table TOUCHABLES;

/* add TOUCHABLE_QUIZZES, has unique(IMAGE_ID) */

create table TOUCHABLE_QUIZZES(
  QUIZ_ID bigint not null,
  foreign key (QUIZ_ID) references QUIZZES(QUIZ_ID) on delete cascade,

  IMAGE_ID bigint not null,
  foreign key (IMAGE_ID) references IMAGES(IMAGE_ID),
  
  unique (IMAGE_ID)
);
