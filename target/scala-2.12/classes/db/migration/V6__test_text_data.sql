insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  0,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  1,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  2,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  3,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  4,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  5,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  6,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  7,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  8,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  9,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  10,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  11,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  12,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  13,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGES (PAGE_NUMBER, BOOK_ID)
values (
  14,
  (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
);

insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=0
  ),
  "Once upon a time, next to a pond, there was a village of frogs.\n“Ribbit, ribbit! Ribbit, ribbit!”\nThe cries of the frogs made it a very noisy village!\n"
);
insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=1
  ),
  "A mother frog and her baby frog lived in a small house in this village.\nThe baby frog was a troublemaker! He did not listen to his mom.\nMom said, “Honey, let’s go to the east!”\nBaby said, “I am going to the west.”\nMom said, “Let’s go to the river!”\nBaby said, “No! I am going to the mountain.”\nThe baby frog never listened to his mom! This made her very upset."
);
insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=2
  ),
  "One bright and sunny day, Mrs. Frog decided to clean the house.\n“Can you bring me the broom?” she asked baby frog.\n“Yes, Mom,” he replied.\nBut baby frog hopped over and brought her a mop instead!\nWhat could Mrs. Frog do but sigh?"
);

insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=3
  ),
  "Then, one rainy day, baby frog said, “Mom! Mom! I want to go outside and play.”\n“But, it’s not safe to play in the rain,” she replied.\nBaby frog kept begging his mother. Eventually she had no choice but to let him play.\n“Stay away from the stream,” Mrs. Frog warned.\nBaby frog nodded. “Yes, mom!”\nBut, of course . . . he went right for the stream."
);

insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=4
  ),
  "The rain stopped after a while. A beautiful rainbow appeared in the sky!\nSo Mrs. Frog and baby frog practiced their singing.\n“Repeat after me,” said Mrs. Frog.\nThen she sang, “Ribbit, ribbit! Ribbit, ribbit!”\n“No! Tibbir, tibbir! Tibbir, tibbir!” sang baby frog.\nHe still wasn’t listening to his mom."
);
insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=5
  ),
  "Mrs. Frog got very upset. \n“What am I going to do with my little troublemaker?” she cried."
);
insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=6
  ),
  "Then one day Mrs. Frog sent baby frog out on an errand.\n“Honey,” she said, “Please ask Mr. Toad if you can borrow some plates.”\nBaby frog hopped on over to Mr. Toad’s house.\nBut when baby frog did not come home, Mrs. Frog got worried. \nShe went out to look for her son.\nShe found him at the playground, coming down the slide!"
);
insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=7
  ),
  "Mrs. Frog scolded baby frog. She tried to talk to him. But it was no use!\nBaby frog’s bad habit kept getting worse and worse.\nMrs. Frog just cried. She sniffled. Tears ran down her cheeks."
);
insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=8
  ),
  "One day, Mrs. Frog became sick. So sick that she fell to the floor.\n“Mom!” shouted baby frog, “I’m so hungry!”\nHe kept asking for food. But Mrs. Frog had no energy."
);
insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=9
  ),
  "Mrs. Frog got sicker and sicker. \nShe thought to herself: “If I die, I want to be buried in the mountain. But my son will surely not listen. He will do the opposite of what I ask for.”\nSo, Mrs. Frog said, “Honey, if I die, bury me near the pond.”\nBaby frog cried, “Mom! Please don’t die! From now on, I will listen to you.”"
);
insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=10
  ),
  "But, eventually, Mrs. Frog passed away.\nBaby frog was very, very sad.\n“If I had known she would die, I would have listened to her,” he whimpered.\nBaby frog cried all day long."
);
insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=11
  ),
  "“This time, I will listen to my mom,” baby frog thought.\nSo he did exactly what his mother had asked for. He buried her near the pond."
);
insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=12
  ),
  "Pitter, patter. Pitter, patter.\nHeavy rain started pouring down in the village.\n“Rain! Rain! Stop pouring!” baby frog cried, “My mom’s grave might be washed away.”\nHe was so worried that he couldn’t stand still. Baby frog hopped here and there."
);
insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=13
  ),
  "But the rain just poured down even harder.\nAnd soon, the pond began to overflow.\n“Oh no! My mom’s grave will be washed away!”\nBaby frog quickly hopped to the pond. He got soaked in the rain.\nWhen he got there, his mom’s grave was nowhere to be found.\nThe rain had already washed it away."
);
insert into BOOK_PAGE_TEXT (
  LANGUAGE_BOOK_ID,
  BOOK_PAGE_ID,
  TEXT
) values (
  (
    select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND LANGUAGE_BOOKS.LANGUAGE_ID=
        (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME="English")
  ),
  (
    select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID=
        (select BOOK_ID from BOOKS where BOOKS.TITLE="Little Frog")
      AND BOOK_PAGES.PAGE_NUMBER=14
  ),
  "“Oh no! Mom!” \nBaby frog sat down and cried. He thought: \n“Mom probably asked to be buried near the pond because I always did the opposite of what she said.”\nHe regretted his bad habit of not listening. But, what could he do about it now?\nFrom that day on, rainy weather reminded baby frog of his mother. He would weep:\n“Ribbit, ribbit. Ribbit, ribbit.\nRibbit, ribbit. Ribbit, ribbit.”\nThis is why frogs cry on rainy days."
);