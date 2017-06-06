create table COUNTRIES(
  NAME varchar(100) not null,
  COUNTRY_ID int not null AUTO_INCREMENT,
  primary key (COUNTRY_ID)
);

create table TAGS(
  NAME varchar(100) not null,
  TAG_ID bigint not null AUTO_INCREMENT,
  primary key (TAG_ID)
);

create table LANGUAGES(
  NAME varchar(100) not null,
  LANGUAGE_ID int not null AUTO_INCREMENT,
  primary key (LANGUAGE_ID)
);

create table BOOKS(
  TITLE varchar(500) not null,
  BASE_URI varchar(500) not null,
  ASPECT_RATIO double not null,
  COUNTRY_ID int not null,
  BOOK_ID bigint not null AUTO_INCREMENT,
  primary key (BOOK_ID),
  foreign key (COUNTRY_ID) references COUNTRIES(COUNTRY_ID)
);

create table BOOK_PAGES(
  PAGE_NUMBER int not null,
  BOOK_ID bigint not null,
  foreign key (BOOK_ID) references BOOKS(BOOK_ID) on delete cascade,
  
  unique (PAGE_NUMBER,BOOK_ID),
  
  BOOK_PAGE_ID bigint not null AUTO_INCREMENT,
  primary key (BOOK_PAGE_ID)
);


create table LANGUAGE_BOOKS(
  BOOK_ID bigint not null,
  foreign key (BOOK_ID) references BOOKS(BOOK_ID) on delete cascade,
  
  LANGUAGE_ID int not null,
  foreign key (LANGUAGE_ID) references LANGUAGES(LANGUAGE_ID),
  
  unique (BOOK_ID, LANGUAGE_ID),
  
  LANGUAGE_BOOK_ID bigint not null AUTO_INCREMENT,
  primary key (LANGUAGE_BOOK_ID)
);

create table BOOK_PAGE_TEXT(
  TEXT varchar(10000) not null,
  LANGUAGE_BOOK_ID bigint not null,
  foreign key (LANGUAGE_BOOK_ID) references LANGUAGE_BOOKS(LANGUAGE_BOOK_ID) on delete cascade,
  
  BOOK_PAGE_ID bigint not null,
  foreign key (BOOK_PAGE_ID) references BOOK_PAGES(BOOK_PAGE_ID),
  
  unique (LANGUAGE_BOOK_ID, BOOK_PAGE_ID)
);

create table BOOK_TAGS(
  BOOK_ID bigint not null,
  foreign key (BOOK_ID) references BOOKS(BOOK_ID) on delete cascade,
  
  TAG_ID bigint not null,
  foreign key (TAG_ID) references TAGS(TAG_ID),
  
  primary key (BOOK_ID, TAG_ID)
);
