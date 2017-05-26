alter table BOOK_PAGE_TEXT drop foreign key BOOK_PAGE_TEXT_ibfk_1;
alter table BOOK_PAGE_TEXT drop foreign key BOOK_PAGE_TEXT_ibfk_2;
alter table BOOK_PAGE_TEXT add foreign key (LANGUAGE_BOOK_ID) references BOOK_LANGUAGES(LANGUAGE_BOOK_ID);
alter table BOOK_PAGE_TEXT add foreign key (BOOK_PAGE_ID) references BOOK_PAGES(BOOK_PAGE_ID) on delete cascade;