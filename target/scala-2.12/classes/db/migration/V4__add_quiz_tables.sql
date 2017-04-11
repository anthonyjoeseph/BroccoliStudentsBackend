create table IMAGES(
  URI varchar(500) not null,
  X_PERCENT double not null,
  Y_PERCENT double not null,
  WIDTH_PERCENT double not null,
  HEIGHT_PERCENT double not null,
  
  BOOK_PAGE_ID bigint not null,
  foreign key (BOOK_PAGE_ID) references BOOK_PAGES(BOOK_PAGE_ID),
  
  IMAGE_ID bigint not null AUTO_INCREMENT,
  primary key (IMAGE_ID)
);

create table QUIZZES(
  IMAGE_ID bigint not null,
  foreign key (IMAGE_ID) references IMAGES(IMAGE_ID),
  
  QUIZ_ID bigint not null AUTO_INCREMENT,
  primary key (QUIZ_ID)
);


create table LANGUAGE_QUIZZES(
  QUIZ_ID bigint not null,
  foreign key (QUIZ_ID) references QUIZZES(QUIZ_ID) on delete cascade,
  
  LANGUAGE_ID int not null,
  foreign key (LANGUAGE_ID) references LANGUAGES(LANGUAGE_ID),
  
  unique (QUIZ_ID, LANGUAGE_ID),
  
  LANGUAGE_QUIZ_ID bigint not null AUTO_INCREMENT,
  primary key (LANGUAGE_QUIZ_ID)
);

create table QUIZ_QUESTIONS(
  QUESTION_NUMBER int not null,
  
  QUIZ_ID bigint not null,
  foreign key (QUIZ_ID) references QUIZZES(QUIZ_ID) on delete cascade,
  
  unique (QUESTION_NUMBER, QUIZ_ID),
  
  QUIZ_QUESTION_ID bigint not null AUTO_INCREMENT,
  primary key (QUIZ_QUESTION_ID)
);

create table QUIZ_RESPONSES(
  RESPONSE_NUMBER int not null,
  
  QUIZ_QUESTION_ID bigint not null,
  foreign key (QUIZ_QUESTION_ID) references QUIZ_QUESTIONS(QUIZ_QUESTION_ID) on delete cascade,
  
  unique (RESPONSE_NUMBER, QUIZ_QUESTION_ID),
  
  QUIZ_RESPONSE_ID bigint not null AUTO_INCREMENT,
  primary key (QUIZ_RESPONSE_ID)
);

create table QUIZ_CORRECT_RESPONSES(
  QUIZ_QUESTION_ID bigint not null,
  foreign key (QUIZ_QUESTION_ID) references QUIZ_QUESTIONS(QUIZ_QUESTION_ID) on delete cascade,
  
  QUIZ_RESPONSE_ID bigint not null,
  foreign key (QUIZ_RESPONSE_ID) references QUIZ_RESPONSES(QUIZ_RESPONSE_ID),
  
  unique (QUIZ_QUESTION_ID)
);

create table QUIZ_STUDENT_RESPONSES(
  USER_ID bigint not null,
  foreign key (USER_ID) references USERS(USER_ID),

  QUIZ_QUESTION_ID bigint not null,
  foreign key (QUIZ_QUESTION_ID) references QUIZ_QUESTIONS(QUIZ_QUESTION_ID),
  
  QUIZ_RESPONSE_ID bigint not null,
  foreign key (QUIZ_RESPONSE_ID) references QUIZ_RESPONSES(QUIZ_RESPONSE_ID),
  
  unique (USER_ID, QUIZ_QUESTION_ID)
);

create table QUIZ_QUESTION_TEXT(
  LANGUAGE_QUIZ_ID bigint not null,
  foreign key (LANGUAGE_QUIZ_ID) references LANGUAGE_QUIZZES(LANGUAGE_QUIZ_ID),
  
  QUIZ_QUESTION_ID bigint not null,
  foreign key (QUIZ_QUESTION_ID) references QUIZ_QUESTIONS(QUIZ_QUESTION_ID) on delete cascade,
  
  unique (LANGUAGE_QUIZ_ID, QUIZ_QUESTION_ID),
  
  TEXT varchar(10000) not null
);

create table QUIZ_RESPONSE_TEXT(
  LANGUAGE_QUIZ_ID bigint not null,
  foreign key (LANGUAGE_QUIZ_ID) references LANGUAGE_QUIZZES(LANGUAGE_QUIZ_ID),
  
  QUIZ_RESPONSE_ID bigint not null,
  foreign key (QUIZ_RESPONSE_ID) references QUIZ_RESPONSES(QUIZ_RESPONSE_ID) on delete cascade,
  
  unique (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID),
  
  TEXT varchar(10000) not null
);