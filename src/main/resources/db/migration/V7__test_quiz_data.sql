insert into IMAGES
  (URI, X_PERCENT, Y_PERCENT, WIDTH_PERCENT, HEIGHT_PERCENT, BOOK_PAGE_ID)
values (
  "frog_outline",
  0.5,
  0.5,
  0.15,
  0.35,
  (select BOOK_PAGE_ID from BOOK_PAGES where BOOK_ID=3 and PAGE_NUMBER=2)
);

insert into IMAGES
  (URI, X_PERCENT, Y_PERCENT, WIDTH_PERCENT, HEIGHT_PERCENT, BOOK_PAGE_ID)
values (
  "frog_outline",
  0.6,
  0.6,
  0.2,
  0.1,
  (select BOOK_PAGE_ID from BOOK_PAGES where BOOK_ID=3 and PAGE_NUMBER=2)
);

insert into IMAGES
  (URI, X_PERCENT, Y_PERCENT, WIDTH_PERCENT, HEIGHT_PERCENT, BOOK_PAGE_ID)
values (
  "spongebob",
  0.5,
  0.5,
  0.15,
  0.35,
  (select BOOK_PAGE_ID from BOOK_PAGES where BOOK_ID=3 and PAGE_NUMBER=2)
);
insert into IMAGES
  (URI, X_PERCENT, Y_PERCENT, WIDTH_PERCENT, HEIGHT_PERCENT, BOOK_PAGE_ID)
values (
  "frog_outline",
  0.29,
  0.26,
  0.335,
  0.29,
  (select BOOK_PAGE_ID from BOOK_PAGES where BOOK_ID=3 and PAGE_NUMBER=2)
);

insert into QUIZZES (IMAGE_ID) values (
  (
    select IMAGES.IMAGE_ID from IMAGES
    inner join (
      select BOOK_PAGE_ID from BOOK_PAGES
      inner join (
        select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog"
      ) b on BOOK_PAGES.BOOK_ID = b.BOOK_ID AND BOOK_PAGES.PAGE_NUMBER=2
    ) bpm on bpm.BOOK_PAGE_ID = IMAGES.BOOK_PAGE_ID AND IMAGES.X_PERCENT=0.5 AND IMAGES.URI="frog_outline"
  )
);
insert into QUIZZES (IMAGE_ID) values (
  (
    select IMAGES.IMAGE_ID from IMAGES
    inner join (
      select BOOK_PAGE_ID from BOOK_PAGES
      inner join (
        select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog"
      ) b on BOOK_PAGES.BOOK_ID = b.BOOK_ID AND BOOK_PAGES.PAGE_NUMBER=2
    ) bpm on bpm.BOOK_PAGE_ID = IMAGES.BOOK_PAGE_ID AND IMAGES.X_PERCENT=0.6
  )
);

insert into LANGUAGE_QUIZZES (LANGUAGE_ID, QUIZ_ID)
values (
  (select LANGUAGE_ID from LANGUAGES where NAME="English"),
  1
);

insert into LANGUAGE_QUIZZES (LANGUAGE_ID, QUIZ_ID)
values (
  (select LANGUAGE_ID from LANGUAGES where NAME="English"),
  2
);

insert into QUIZ_QUESTIONS (QUESTION_NUMBER, QUIZ_ID)
values (
  1,
  1
);

insert into QUIZ_QUESTIONS (QUESTION_NUMBER, QUIZ_ID)
values (
  2,
  1
);

insert into QUIZ_QUESTIONS (QUESTION_NUMBER, QUIZ_ID)
values (
  1,
  2
);

insert into QUIZ_QUESTION_TEXT (LANGUAGE_QUIZ_ID, QUIZ_QUESTION_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=1
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=1),
  "What kind of day is it outside?"
);

insert into QUIZ_QUESTION_TEXT (LANGUAGE_QUIZ_ID, QUIZ_QUESTION_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=1
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=2),
  "What kind of cleaning tool did Mrs. Frog ask Little Frog to Bring?"
);

insert into QUIZ_QUESTION_TEXT (LANGUAGE_QUIZ_ID, QUIZ_QUESTION_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=2
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=2 AND QUESTION_NUMBER=1),
  "Did Little Frog bring what Mrs. Frog wanted him to bring?"
);

insert into QUIZ_RESPONSES (RESPONSE_NUMBER, QUIZ_QUESTION_ID)
values (
  1,
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=1)
);

insert into QUIZ_RESPONSES (RESPONSE_NUMBER, QUIZ_QUESTION_ID)
values (
  2,
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=1)
);

insert into QUIZ_RESPONSES (RESPONSE_NUMBER, QUIZ_QUESTION_ID)
values (
  3,
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=1)
);

insert into QUIZ_RESPONSES (RESPONSE_NUMBER, QUIZ_QUESTION_ID)
values (
  4,
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=1)
);

insert into QUIZ_RESPONSES (RESPONSE_NUMBER, QUIZ_QUESTION_ID)
values (
  1,
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=2)
);

insert into QUIZ_RESPONSES (RESPONSE_NUMBER, QUIZ_QUESTION_ID)
values (
  2,
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=2)
);

insert into QUIZ_RESPONSES (RESPONSE_NUMBER, QUIZ_QUESTION_ID)
values (
  3,
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=2)
);

insert into QUIZ_RESPONSES (RESPONSE_NUMBER, QUIZ_QUESTION_ID)
values (
  4,
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=2)
);

insert into QUIZ_RESPONSES (RESPONSE_NUMBER, QUIZ_QUESTION_ID)
values (
  1,
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=2 AND QUESTION_NUMBER=1)
);

insert into QUIZ_RESPONSES (RESPONSE_NUMBER, QUIZ_QUESTION_ID)
values (
  2,
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=2 AND QUESTION_NUMBER=1)
);


insert into QUIZ_RESPONSES (RESPONSE_NUMBER, QUIZ_QUESTION_ID)
values (
  3,
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=2 AND QUESTION_NUMBER=1)
);


insert into QUIZ_RESPONSES (RESPONSE_NUMBER, QUIZ_QUESTION_ID)
values (
  4,
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=2 AND QUESTION_NUMBER=1)
);

insert into QUIZ_RESPONSE_TEXT (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=1
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=1
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=1
  ),
  "Nighttime"
);

insert into QUIZ_RESPONSE_TEXT (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=1
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=1
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=2
  ),
  "Cloudy"
);

insert into QUIZ_RESPONSE_TEXT (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=1
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=1
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=3
  ),
  "Bright and cold"
);

insert into QUIZ_RESPONSE_TEXT (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=1
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=1
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=4
  ),
  "Bright, Sunny"
);

insert into QUIZ_RESPONSE_TEXT (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=1
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=2
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=1
  ),
  "Windex"
);

insert into QUIZ_RESPONSE_TEXT (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=1
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=2
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=2
  ),
  "Broom"
);

insert into QUIZ_RESPONSE_TEXT (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=1
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=2
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=3
  ),
  "Mop"
);

insert into QUIZ_RESPONSE_TEXT (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=1
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=2
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=4
  ),
  "Paper Towel"
);

insert into QUIZ_RESPONSE_TEXT (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=2
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=2 AND QUESTION_NUMBER=1
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=1
  ),
  "Yes"
);

insert into QUIZ_RESPONSE_TEXT (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=2
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=2 AND QUESTION_NUMBER=1
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=2
  ),
  "No"
);

insert into QUIZ_RESPONSE_TEXT (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=2
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=2 AND QUESTION_NUMBER=1
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=3
  ),
  "Maybe"
);

insert into QUIZ_RESPONSE_TEXT (LANGUAGE_QUIZ_ID, QUIZ_RESPONSE_ID, TEXT)
values (
  (
    select lq.LANGUAGE_QUIZ_ID
      from (
        select LANGUAGE_ID, LANGUAGE_QUIZ_ID from LANGUAGE_QUIZZES where QUIZ_ID=2
      ) lq
      inner join (
        select LANGUAGE_ID from LANGUAGES where NAME="English"
      ) l
      on lq.LANGUAGE_ID = l.LANGUAGE_ID
  ),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=2 AND QUESTION_NUMBER=1
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=4
  ),
  "Depends"
);

insert into QUIZ_CORRECT_RESPONSES (QUIZ_QUESTION_ID, QUIZ_RESPONSE_ID)
values (
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=1),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=1
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=4
  )
);

insert into QUIZ_CORRECT_RESPONSES (QUIZ_QUESTION_ID, QUIZ_RESPONSE_ID)
values (
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=2),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=1 AND QUESTION_NUMBER=2
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=2
  )
);

insert into QUIZ_CORRECT_RESPONSES (QUIZ_QUESTION_ID, QUIZ_RESPONSE_ID)
values (
  (select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=2 AND QUESTION_NUMBER=1),
  (
    select qr.QUIZ_RESPONSE_ID from (
      select QUIZ_RESPONSE_ID, RESPONSE_NUMBER from QUIZ_RESPONSES
        inner join (
          select QUIZ_QUESTION_ID from QUIZ_QUESTIONS where QUIZ_ID=2 AND QUESTION_NUMBER=1
        ) qq
        on QUIZ_RESPONSES.QUIZ_QUESTION_ID = qq.QUIZ_QUESTION_ID
    ) qr where qr.RESPONSE_NUMBER=1
  )
);