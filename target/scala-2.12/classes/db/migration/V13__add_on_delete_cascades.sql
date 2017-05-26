alter table LANGUAGE_BOOKS RENAME TO BOOK_LANGUAGES;
alter table LANGUAGE_QUIZZES RENAME TO QUIZ_LANGUAGES;

alter table BOOK_PAGE_IMAGES drop foreign key BOOK_PAGE_IMAGES_ibfk_1;
alter table BOOK_PAGE_IMAGES add foreign key (BOOK_PAGE_ID) references BOOK_PAGES(BOOK_PAGE_ID) on delete cascade;

alter table BOOK_PAGE_MUSIC drop foreign key BOOK_PAGE_MUSIC_ibfk_1;
alter table BOOK_PAGE_MUSIC add foreign key (BOOK_PAGE_ID) references BOOK_PAGES(BOOK_PAGE_ID) on delete cascade;

alter table TOUCHABLES drop foreign key TOUCHABLES_ibfk_1;
alter table TOUCHABLES add foreign key (IMAGE_ID) references IMAGES(IMAGE_ID) on delete cascade;

alter table QUIZZES drop foreign key QUIZZES_ibfk_1;
alter table QUIZZES drop column IMAGE_ID;
alter table QUIZZES add foreign key (TOUCHABLE_ID) references TOUCHABLES(TOUCHABLE_ID) on delete cascade;

alter table TOUCHABLE_ANIMATIONS drop foreign key TOUCHABLE_ANIMATIONS_ibfk_1;
alter table TOUCHABLE_ANIMATIONS drop foreign key TOUCHABLE_ANIMATIONS_ibfk_2;
alter table TOUCHABLE_ANIMATIONS add foreign key (IMAGE_ID) references IMAGES(IMAGE_ID);
alter table TOUCHABLE_ANIMATIONS add foreign key (TOUCHABLE_ID) references TOUCHABLES(TOUCHABLE_ID) on delete cascade;

alter table TOUCHABLE_PRONUNCIATIONS drop foreign key TOUCHABLE_PRONUNCIATIONS_ibfk_1;
alter table TOUCHABLE_PRONUNCIATIONS add foreign key (TOUCHABLE_ID) references TOUCHABLES(TOUCHABLE_ID) on delete cascade;

alter table TOUCHABLE_SOUNDS drop foreign key TOUCHABLE_SOUNDS_ibfk_1;
alter table TOUCHABLE_SOUNDS add foreign key (TOUCHABLE_ID) references TOUCHABLES(TOUCHABLE_ID) on delete cascade;

drop table TOUCHABLE_TOUCH_ACTIONS;
drop table TOUCH_ACTIONS;