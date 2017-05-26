insert into MUSIC (URI) values (
  "p1.mp3"
);
insert into MUSIC (URI) values (
  "p2+p3.mp3"
);
insert into MUSIC (URI) values (
  "p4+p5.mp3"
);
insert into MUSIC (URI) values (
  "p6.mp3"
);
insert into MUSIC (URI) values (
  "p7.mp3"
);
insert into MUSIC (URI) values (
  "p8+p14.mp3"
);
insert into MUSIC (URI) values (
  "p9+p10.mp3"
);
insert into MUSIC (URI) values (
  "p11.mp3"
);
insert into MUSIC (URI) values (
  "p12+p13.mp3"
);
insert into MUSIC (URI) values (
  "p15.mp3"
);

insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=0),
  (select MUSIC_ID from MUSIC where URI="p1.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=1),
  (select MUSIC_ID from MUSIC where URI="p2+p3.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=2),
  (select MUSIC_ID from MUSIC where URI="p2+p3.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=3),
  (select MUSIC_ID from MUSIC where URI="p4+p5.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=4),
  (select MUSIC_ID from MUSIC where URI="p4+p5.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=5),
  (select MUSIC_ID from MUSIC where URI="p6.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=6),
  (select MUSIC_ID from MUSIC where URI="p7.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=7),
  (select MUSIC_ID from MUSIC where URI="p8+p14.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=8),
  (select MUSIC_ID from MUSIC where URI="p9+p10.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=9),
  (select MUSIC_ID from MUSIC where URI="p9+p10.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=10),
  (select MUSIC_ID from MUSIC where URI="p11.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=11),
  (select MUSIC_ID from MUSIC where URI="p12+p13.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=12),
  (select MUSIC_ID from MUSIC where URI="p12+p13.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=13),
  (select MUSIC_ID from MUSIC where URI="p8+p14.mp3")
);
insert into BOOK_PAGE_MUSIC (BOOK_PAGE_ID, MUSIC_ID) values (
  (select BOOK_PAGE_ID from BOOK_PAGES where
    BOOK_PAGES.BOOK_ID=
      (select BOOK_ID from BOOKS where TITLE="Little Frog")
    and BOOK_PAGES.PAGE_NUMBER=14),
  (select MUSIC_ID from MUSIC where URI="p15.mp3")
);
