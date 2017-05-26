alter table BOOK_LANGUAGES drop foreign key BOOK_LANGUAGES_ibfk_1;
alter table BOOK_LANGUAGES add foreign key (BOOK_ID) references BOOKS(BOOK_ID) on delete cascade;