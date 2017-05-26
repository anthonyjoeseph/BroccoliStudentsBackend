alter table BOOK_PAGES add MUSIC_ID BIGINT;
alter table BOOK_PAGES add foreign key (MUSIC_ID) references MUSIC(MUSIC_ID);
drop table BOOK_PAGE_MUSIC;