--
-- PostgreSQL database dump
--

-- Dumped from database version 17.0
-- Dumped by pg_dump version 17.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: ADRESA; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."ADRESA" (
    "ID_Adresa" integer NOT NULL,
    "ID_persoana" integer,
    strada character varying(50) NOT NULL,
    numarul character varying(5) NOT NULL,
    oras character varying(50) NOT NULL,
    judet character varying(50) NOT NULL,
    cod_postal character(6) NOT NULL
);


ALTER TABLE public."ADRESA" OWNER TO postgres;

--
-- Name: ADRESA_ID_Adresa_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."ADRESA_ID_Adresa_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."ADRESA_ID_Adresa_seq" OWNER TO postgres;

--
-- Name: ADRESA_ID_Adresa_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."ADRESA_ID_Adresa_seq" OWNED BY public."ADRESA"."ID_Adresa";


--
-- Name: BUNURI; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."BUNURI" (
    "ID_bun" integer NOT NULL,
    tip_bun character varying(50) NOT NULL,
    valoare real NOT NULL,
    data_achizitiei date NOT NULL
);


ALTER TABLE public."BUNURI" OWNER TO postgres;

--
-- Name: BUNURI_ID_bun_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."BUNURI_ID_bun_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."BUNURI_ID_bun_seq" OWNER TO postgres;

--
-- Name: BUNURI_ID_bun_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."BUNURI_ID_bun_seq" OWNED BY public."BUNURI"."ID_bun";


--
-- Name: CONTURI_BANCARE; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."CONTURI_BANCARE" (
    "ID_cont" integer NOT NULL,
    "ID_persoana" integer,
    "IBAN" character(24) NOT NULL,
    banca character varying(50) NOT NULL,
    tip_cont character varying(50) NOT NULL
);


ALTER TABLE public."CONTURI_BANCARE" OWNER TO postgres;

--
-- Name: CONTURI_BANCARE_ID_cont_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."CONTURI_BANCARE_ID_cont_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."CONTURI_BANCARE_ID_cont_seq" OWNER TO postgres;

--
-- Name: CONTURI_BANCARE_ID_cont_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."CONTURI_BANCARE_ID_cont_seq" OWNED BY public."CONTURI_BANCARE"."ID_cont";


--
-- Name: PERSOANA; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."PERSOANA" (
    "ID_persoana" integer NOT NULL,
    nume character varying(50) NOT NULL,
    prenume character varying(50) NOT NULL,
    "CNP" character(13) NOT NULL,
    data_nasterii date,
    sex character(1) DEFAULT 'M'::bpchar,
    email character varying(50) NOT NULL,
    telefon character varying(20) NOT NULL,
    CONSTRAINT "PERSOANA_sex_check" CHECK ((sex = ANY (ARRAY['M'::bpchar, 'F'::bpchar])))
);


ALTER TABLE public."PERSOANA" OWNER TO postgres;

--
-- Name: PERSOANA_ID_persoana_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."PERSOANA_ID_persoana_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."PERSOANA_ID_persoana_seq" OWNER TO postgres;

--
-- Name: PERSOANA_ID_persoana_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."PERSOANA_ID_persoana_seq" OWNED BY public."PERSOANA"."ID_persoana";


--
-- Name: PERSOANE_BUNURI; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."PERSOANE_BUNURI" (
    "ID_persoane_bunuri" integer NOT NULL,
    "ID_persoana" integer,
    "ID_bun" integer
);


ALTER TABLE public."PERSOANE_BUNURI" OWNER TO postgres;

--
-- Name: PERSOANE_BUNURI_ID_persoane_bunuri_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."PERSOANE_BUNURI_ID_persoane_bunuri_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."PERSOANE_BUNURI_ID_persoane_bunuri_seq" OWNER TO postgres;

--
-- Name: PERSOANE_BUNURI_ID_persoane_bunuri_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."PERSOANE_BUNURI_ID_persoane_bunuri_seq" OWNED BY public."PERSOANE_BUNURI"."ID_persoane_bunuri";


--
-- Name: TAXE; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."TAXE" (
    "ID_taxa" integer NOT NULL,
    "ID_persoana" integer,
    tip_taxa character varying(50) NOT NULL,
    suma_taxa real NOT NULL,
    data_scadenta date NOT NULL,
    platit character varying(10) DEFAULT 'nu'::character varying,
    data_platii date,
    CONSTRAINT "TAXE_platit_check" CHECK (((platit)::text = ANY ((ARRAY['da'::character varying, 'nu'::character varying])::text[])))
);


ALTER TABLE public."TAXE" OWNER TO postgres;

--
-- Name: TAXE_ID_taxa_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."TAXE_ID_taxa_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."TAXE_ID_taxa_seq" OWNER TO postgres;

--
-- Name: TAXE_ID_taxa_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."TAXE_ID_taxa_seq" OWNED BY public."TAXE"."ID_taxa";


--
-- Name: USERS; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."USERS" (
    "ID_user" integer NOT NULL,
    "ID_persoana" integer,
    username character varying(50) NOT NULL,
    parola character varying(255) NOT NULL,
    email character varying(50) NOT NULL,
    rol character varying(10) DEFAULT 'user'::character varying NOT NULL,
    CONSTRAINT "USERS_rol_check" CHECK (((rol)::text = ANY ((ARRAY['admin'::character varying, 'user'::character varying])::text[])))
);


ALTER TABLE public."USERS" OWNER TO postgres;

--
-- Name: USERS_ID_user_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."USERS_ID_user_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."USERS_ID_user_seq" OWNER TO postgres;

--
-- Name: USERS_ID_user_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."USERS_ID_user_seq" OWNED BY public."USERS"."ID_user";


--
-- Name: VENIT; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."VENIT" (
    "ID_venit" integer NOT NULL,
    "ID_persoana" integer,
    tip_venit character varying(50) NOT NULL,
    suma_venit real NOT NULL,
    data_venit date
);


ALTER TABLE public."VENIT" OWNER TO postgres;

--
-- Name: VENIT_ID_venit_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."VENIT_ID_venit_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public."VENIT_ID_venit_seq" OWNER TO postgres;

--
-- Name: VENIT_ID_venit_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."VENIT_ID_venit_seq" OWNED BY public."VENIT"."ID_venit";


--
-- Name: ADRESA ID_Adresa; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."ADRESA" ALTER COLUMN "ID_Adresa" SET DEFAULT nextval('public."ADRESA_ID_Adresa_seq"'::regclass);


--
-- Name: BUNURI ID_bun; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."BUNURI" ALTER COLUMN "ID_bun" SET DEFAULT nextval('public."BUNURI_ID_bun_seq"'::regclass);


--
-- Name: CONTURI_BANCARE ID_cont; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."CONTURI_BANCARE" ALTER COLUMN "ID_cont" SET DEFAULT nextval('public."CONTURI_BANCARE_ID_cont_seq"'::regclass);


--
-- Name: PERSOANA ID_persoana; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PERSOANA" ALTER COLUMN "ID_persoana" SET DEFAULT nextval('public."PERSOANA_ID_persoana_seq"'::regclass);


--
-- Name: PERSOANE_BUNURI ID_persoane_bunuri; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PERSOANE_BUNURI" ALTER COLUMN "ID_persoane_bunuri" SET DEFAULT nextval('public."PERSOANE_BUNURI_ID_persoane_bunuri_seq"'::regclass);


--
-- Name: TAXE ID_taxa; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."TAXE" ALTER COLUMN "ID_taxa" SET DEFAULT nextval('public."TAXE_ID_taxa_seq"'::regclass);


--
-- Name: USERS ID_user; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."USERS" ALTER COLUMN "ID_user" SET DEFAULT nextval('public."USERS_ID_user_seq"'::regclass);


--
-- Name: VENIT ID_venit; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."VENIT" ALTER COLUMN "ID_venit" SET DEFAULT nextval('public."VENIT_ID_venit_seq"'::regclass);


--
-- Data for Name: ADRESA; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."ADRESA" ("ID_Adresa", "ID_persoana", strada, numarul, oras, judet, cod_postal) FROM stdin;
2	13	Splaiul Independentei	290	Bucuresti	Ilfov	060029
3	16	Cpt. Zaganescu	31	Moinesti	Bacau	605400
4	17	Splaiul Independentei	290	Bucuresti	Ilfov	060029
5	18	Splaiul Independentei	290	Bucuresti	Ilfov	060029
1	11	Cpt. Zaganescu	31	Moinesti	Bacau	605400
\.


--
-- Data for Name: BUNURI; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."BUNURI" ("ID_bun", tip_bun, valoare, data_achizitiei) FROM stdin;
3	apartament	15000	2024-11-01
4	autoturism	60000	2024-10-29
7	apartament	63000	2024-06-06
10	casa	60000	2024-11-01
11	teren	100000	2024-10-03
12	autoturism	7000	2024-11-01
15	autoturism	60000	2025-01-04
16	teren	78000	2025-01-05
18	autoturism	969696	2025-01-09
19	apartament	90000	2024-12-09
\.


--
-- Data for Name: CONTURI_BANCARE; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."CONTURI_BANCARE" ("ID_cont", "ID_persoana", "IBAN", banca, tip_cont) FROM stdin;
5	16	RO49AAAA1B31007593840014	BCR	salariu
4	11	RO49AAAA1B31007593840000	BCR	curent
1	11	RO49AAAA1B31007593840012	BCR	depozit
6	13	RO49AAAA1B31007593841234	Revolut	economii
8	13	RO49AAAA1B31007593845790	BRD	depozit
9	11	RO49AAAA1B31007593987654	BRD	curent
\.


--
-- Data for Name: PERSOANA; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."PERSOANA" ("ID_persoana", nume, prenume, "CNP", data_nasterii, sex, email, telefon) FROM stdin;
13	Popescu	Ion	5010101012345	2024-11-06	M	user1@user.ro	0712345678
16	Bolohan	Tudor	5020434567090	2002-04-02	M	tudorbolohan20@gmail.com	0725010101
17	Ionescu	Andreea	6010101123456	2003-02-05	F	user2@user.ro	0712365478
11	Bolohan	Cristian	5030457065715	2003-09-07	M	cristibolohan2003@gmail.com	0709876543
18	Anghel	Marian	5010101019090	2018-01-04	M	userTest@user.ro	0757909090
\.


--
-- Data for Name: PERSOANE_BUNURI; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."PERSOANE_BUNURI" ("ID_persoane_bunuri", "ID_persoana", "ID_bun") FROM stdin;
3	13	3
4	13	4
7	13	7
10	11	10
11	16	10
13	16	11
14	16	12
17	11	15
18	11	16
20	16	18
21	13	19
\.


--
-- Data for Name: TAXE; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."TAXE" ("ID_taxa", "ID_persoana", tip_taxa, suma_taxa, data_scadenta, platit, data_platii) FROM stdin;
6	13	impozit_auto	6000	2025-12-31	nu	\N
9	13	impozit_auto	6000	2025-12-31	nu	\N
8	11	impozit_teren	7800	2025-12-31	da	2025-01-04
2	11	impozit_teren	7800	2025-12-31	da	2025-01-04
3	16	impozit_venit	450	2025-12-31	da	2025-01-04
4	16	impozit_venit	450	2025-12-31	da	2025-01-04
1	11	impozit_auto	6000	2025-12-31	da	2025-01-04
5	13	impozit_venit	850	2025-12-31	da	2025-01-04
10	11	impozit_teren	7800	2025-12-31	nu	\N
11	16	impozit_auto	700.6	2025-12-31	nu	\N
13	11	impozit_teren	700	2025-12-31	nu	\N
14	11	impozit_venit	650	2025-12-31	nu	\N
12	11	impozit_auto	0	2025-12-31	da	2025-01-05
7	13	impozit_cladire	6300	2025-12-31	da	2025-01-05
15	11	impozit_teren	7800	2025-12-31	nu	\N
\.


--
-- Data for Name: USERS; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."USERS" ("ID_user", "ID_persoana", username, parola, email, rol) FROM stdin;
8	\N	admin	$2a$10$2kVTaw93FaPPMTX4OHWWw.jBBbaaYT7rf45VCuO5ik8wi4zWqYXPG	admin@admin.ro	admin
10	11	bolo03	$2a$10$lcruycx4Dy3kBAX4n5tIzOfJ6Y2GNZtM.rnNlthXIs.7mWrOivtuG	cristibolo03@gmail.com	user
12	13	user1	$2a$10$QUcC1NRcXGMuYzfGbrNkXO0ya2rWVH21LE4peUfevtW4zPyscBsGi	user1@user.com	user
15	16	tudor02	$2a$10$3Fu/RSjonS2DiFUJDKaq.e5BfJLofWoAVTjyzLW9S32UoEuYqy5/m	tudorbolohan20@gmail.com	user
16	17	user2	$2a$10$CRqrHHbQb1fRjDtp4B8m4uH1m6mfyhu/YG8zzbNzEaQzjTJJ.aY6e	user2@user.ro	user
17	\N	andreea	$2a$10$wFX4G9HyqqkruBTCQfVsZukadJtCxOMXBZ7EWS0mdfhs.GtbjVziO	andreea@gmail.com	user
18	18	userTest	$2a$10$l9xMKmOK.ozKF6oF71ljCeuuqjAQ8JBzgWj0zvaZxlZWyPJGpzwoe	userTest@user.ro	user
20	\N	userTest2	$2a$10$TCLq78L2lcuS4a4X1f7NleToB22gZ7eqkdn3KM0NM5mhTSXozYHsW	userTest2@user.com	user
21	\N	userTest3	$2a$10$7UrfjpinmKRG1P3UxxNMFunZT/qWt0jgB/KnbMdzkLbLTfctYQmVK	userTest3@user.com	user
22	\N	testUSER	$2a$10$KuRnpmHt2ft5T9L7OX58juygLzo0WAiIl.ilMTpDMqOKJvGH6Hz/i	testUSER@user.com	user
\.


--
-- Data for Name: VENIT; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."VENIT" ("ID_venit", "ID_persoana", tip_venit, suma_venit, data_venit) FROM stdin;
10	16	chirie	1500	2024-11-03
19	13	chirie	850	2024-11-02
9	11	investitii	7500	2024-11-26
20	11	dividende	1750	2023-07-06
1	11	salariu	6500	2024-11-06
15	16	dividende	1600	2024-11-06
29	16	salariu	4500	2024-12-11
30	13	chirie	750	2024-12-31
32	13	salariu	8500	2024-10-30
33	11	chirie	1600	2024-11-13
\.


--
-- Name: ADRESA_ID_Adresa_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."ADRESA_ID_Adresa_seq"', 5, true);


--
-- Name: BUNURI_ID_bun_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."BUNURI_ID_bun_seq"', 19, true);


--
-- Name: CONTURI_BANCARE_ID_cont_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."CONTURI_BANCARE_ID_cont_seq"', 10, true);


--
-- Name: PERSOANA_ID_persoana_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."PERSOANA_ID_persoana_seq"', 18, true);


--
-- Name: PERSOANE_BUNURI_ID_persoane_bunuri_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."PERSOANE_BUNURI_ID_persoane_bunuri_seq"', 21, true);


--
-- Name: TAXE_ID_taxa_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."TAXE_ID_taxa_seq"', 15, true);


--
-- Name: USERS_ID_user_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."USERS_ID_user_seq"', 22, true);


--
-- Name: VENIT_ID_venit_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."VENIT_ID_venit_seq"', 33, true);


--
-- Name: ADRESA ADRESA_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."ADRESA"
    ADD CONSTRAINT "ADRESA_pkey" PRIMARY KEY ("ID_Adresa");


--
-- Name: BUNURI BUNURI_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."BUNURI"
    ADD CONSTRAINT "BUNURI_pkey" PRIMARY KEY ("ID_bun");


--
-- Name: CONTURI_BANCARE CONTURI_BANCARE_IBAN_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."CONTURI_BANCARE"
    ADD CONSTRAINT "CONTURI_BANCARE_IBAN_key" UNIQUE ("IBAN");


--
-- Name: CONTURI_BANCARE CONTURI_BANCARE_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."CONTURI_BANCARE"
    ADD CONSTRAINT "CONTURI_BANCARE_pkey" PRIMARY KEY ("ID_cont");


--
-- Name: PERSOANA PERSOANA_CNP_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PERSOANA"
    ADD CONSTRAINT "PERSOANA_CNP_key" UNIQUE ("CNP");


--
-- Name: PERSOANA PERSOANA_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PERSOANA"
    ADD CONSTRAINT "PERSOANA_pkey" PRIMARY KEY ("ID_persoana");


--
-- Name: PERSOANE_BUNURI PERSOANE_BUNURI_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PERSOANE_BUNURI"
    ADD CONSTRAINT "PERSOANE_BUNURI_pkey" PRIMARY KEY ("ID_persoane_bunuri");


--
-- Name: TAXE TAXE_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."TAXE"
    ADD CONSTRAINT "TAXE_pkey" PRIMARY KEY ("ID_taxa");


--
-- Name: USERS USERS_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."USERS"
    ADD CONSTRAINT "USERS_email_key" UNIQUE (email);


--
-- Name: USERS USERS_parola_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."USERS"
    ADD CONSTRAINT "USERS_parola_key" UNIQUE (parola);


--
-- Name: USERS USERS_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."USERS"
    ADD CONSTRAINT "USERS_pkey" PRIMARY KEY ("ID_user");


--
-- Name: USERS USERS_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."USERS"
    ADD CONSTRAINT "USERS_username_key" UNIQUE (username);


--
-- Name: VENIT VENIT_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."VENIT"
    ADD CONSTRAINT "VENIT_pkey" PRIMARY KEY ("ID_venit");


--
-- Name: ADRESA ADRESA_ID_persoana_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."ADRESA"
    ADD CONSTRAINT "ADRESA_ID_persoana_fkey" FOREIGN KEY ("ID_persoana") REFERENCES public."PERSOANA"("ID_persoana");


--
-- Name: CONTURI_BANCARE CONTURI_BANCARE_ID_persoana_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."CONTURI_BANCARE"
    ADD CONSTRAINT "CONTURI_BANCARE_ID_persoana_fkey" FOREIGN KEY ("ID_persoana") REFERENCES public."PERSOANA"("ID_persoana");


--
-- Name: PERSOANE_BUNURI PERSOANE_BUNURI_ID_bun_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PERSOANE_BUNURI"
    ADD CONSTRAINT "PERSOANE_BUNURI_ID_bun_fkey" FOREIGN KEY ("ID_bun") REFERENCES public."BUNURI"("ID_bun");


--
-- Name: PERSOANE_BUNURI PERSOANE_BUNURI_ID_persoana_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."PERSOANE_BUNURI"
    ADD CONSTRAINT "PERSOANE_BUNURI_ID_persoana_fkey" FOREIGN KEY ("ID_persoana") REFERENCES public."PERSOANA"("ID_persoana");


--
-- Name: TAXE TAXE_ID_persoana_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."TAXE"
    ADD CONSTRAINT "TAXE_ID_persoana_fkey" FOREIGN KEY ("ID_persoana") REFERENCES public."PERSOANA"("ID_persoana");


--
-- Name: USERS USERS_ID_persoana_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."USERS"
    ADD CONSTRAINT "USERS_ID_persoana_fkey" FOREIGN KEY ("ID_persoana") REFERENCES public."PERSOANA"("ID_persoana");


--
-- Name: VENIT VENIT_ID_persoana_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."VENIT"
    ADD CONSTRAINT "VENIT_ID_persoana_fkey" FOREIGN KEY ("ID_persoana") REFERENCES public."PERSOANA"("ID_persoana");


--
-- PostgreSQL database dump complete
--

