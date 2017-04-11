insert into LANGUAGES (NAME) values ("English");
insert into LANGUAGES (NAME) values ("Spanish");
insert into LANGUAGES (NAME) values ("Korean");

insert into TAGS (NAME) values ("frog");
insert into TAGS (NAME) values ("genie");
insert into TAGS (NAME) values ("fable");
insert into TAGS (NAME) values ("moral");
insert into TAGS (NAME) values ("family");
insert into TAGS (NAME) values ("animals");
insert into TAGS (NAME) values ("lamp");
insert into TAGS (NAME) values ("kingdom");
insert into TAGS (NAME) values ("islam");

insert into COUNTRIES (NAME) values ("Korea");
insert into COUNTRIES (NAME) values ("Iraq");
insert into COUNTRIES (NAME) values ("USA");
insert into COUNTRIES (NAME) values ("France");

insert into BOOKS (TITLE, BASE_URI, ASPECT_RATIO, COUNTRY_ID)
values (
  "Little Frog",
  "littleFrog",
  1.64207980652963,
  (select COUNTRY_ID from COUNTRIES where COUNTRIES.NAME = "Korea")
);

insert into BOOKS (TITLE, BASE_URI, ASPECT_RATIO, COUNTRY_ID)
values (
  "Aladdin",
  "aladdin",
  1.64207980652963,
  (select COUNTRY_ID from COUNTRIES where COUNTRIES.NAME = "Iraq")
);

insert into LANGUAGE_BOOKS (BOOK_ID, LANGUAGE_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Little Frog"),
  (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME = "English")
);

insert into LANGUAGE_BOOKS (BOOK_ID, LANGUAGE_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Little Frog"),
  (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME = "Korean")
);

insert into LANGUAGE_BOOKS (BOOK_ID, LANGUAGE_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Aladdin"),
  (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME = "English")
);

insert into LANGUAGE_BOOKS (BOOK_ID, LANGUAGE_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Aladdin"),
  (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME = "Spanish")
);

insert into BOOK_TAGS (BOOK_ID, TAG_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Little Frog"),
  (select TAG_ID from TAGS where TAGS.NAME = "family")
);

insert into BOOK_TAGS (BOOK_ID, TAG_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Little Frog"),
  (select TAG_ID from TAGS where TAGS.NAME = "fable")
);

insert into BOOK_TAGS (BOOK_ID, TAG_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Little Frog"),
  (select TAG_ID from TAGS where TAGS.NAME = "moral")
);

insert into BOOK_TAGS (BOOK_ID, TAG_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Little Frog"),
  (select TAG_ID from TAGS where TAGS.NAME = "frog")
);

insert into BOOK_TAGS (BOOK_ID, TAG_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Little Frog"),
  (select TAG_ID from TAGS where TAGS.NAME = "animals")
);

insert into BOOK_TAGS (BOOK_ID, TAG_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Aladdin"),
  (select TAG_ID from TAGS where TAGS.NAME = "genie")
);

insert into BOOK_TAGS (BOOK_ID, TAG_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Aladdin"),
  (select TAG_ID from TAGS where TAGS.NAME = "fable")
);

insert into BOOK_TAGS (BOOK_ID, TAG_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Aladdin"),
  (select TAG_ID from TAGS where TAGS.NAME = "moral")
);

insert into BOOK_TAGS (BOOK_ID, TAG_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Aladdin"),
  (select TAG_ID from TAGS where TAGS.NAME = "lamp")
);

insert into BOOK_TAGS (BOOK_ID, TAG_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Aladdin"),
  (select TAG_ID from TAGS where TAGS.NAME = "kingdom")
);

insert into BOOK_TAGS (BOOK_ID, TAG_ID)
values (
  (select BOOK_ID from BOOKS where BOOKS.TITLE = "Aladdin"),
  (select TAG_ID from TAGS where TAGS.NAME = "islam")
);