--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4 (Debian 13.4-4.pgdg110+1)
-- Dumped by pg_dump version 13.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
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
-- Name: car_brand; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.car_brand (
    id integer NOT NULL,
    name text NOT NULL,
    value_class text
);


ALTER TABLE public.car_brand OWNER TO root;

--
-- Name: car_brand_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.car_brand_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.car_brand_id_seq OWNER TO root;

--
-- Name: car_brand_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.car_brand_id_seq OWNED BY public.car_brand.id;


--
-- Name: car_model; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.car_model (
    id integer NOT NULL,
    name text NOT NULL,
    brand integer NOT NULL,
    base_price integer NOT NULL
);


ALTER TABLE public.car_model OWNER TO root;

--
-- Name: car_model_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.car_model_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.car_model_id_seq OWNER TO root;

--
-- Name: car_model_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.car_model_id_seq OWNED BY public.car_model.id;


--
-- Name: stock; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.stock (
    id integer NOT NULL,
    store integer,
    car_model integer,
    price integer,
    quantity integer
);


ALTER TABLE public.stock OWNER TO root;

--
-- Name: stock_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.stock_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.stock_id_seq OWNER TO root;

--
-- Name: stock_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.stock_id_seq OWNED BY public.stock.id;


--
-- Name: store; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.store (
    id integer NOT NULL,
    name text,
    location text
);


ALTER TABLE public.store OWNER TO root;

--
-- Name: store_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.store_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.store_id_seq OWNER TO root;

--
-- Name: store_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.store_id_seq OWNED BY public.store.id;


--
-- Name: car_brand id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.car_brand ALTER COLUMN id SET DEFAULT nextval('public.car_brand_id_seq'::regclass);


--
-- Name: car_model id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.car_model ALTER COLUMN id SET DEFAULT nextval('public.car_model_id_seq'::regclass);


--
-- Name: stock id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.stock ALTER COLUMN id SET DEFAULT nextval('public.stock_id_seq'::regclass);


--
-- Name: store id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.store ALTER COLUMN id SET DEFAULT nextval('public.store_id_seq'::regclass);


--
-- Name: car_brand car_brand_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.car_brand
    ADD CONSTRAINT car_brand_pk PRIMARY KEY (id);


--
-- Name: car_model car_model_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.car_model
    ADD CONSTRAINT car_model_pk PRIMARY KEY (id);


--
-- Name: stock stock_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.stock
    ADD CONSTRAINT stock_pk PRIMARY KEY (id);


--
-- Name: store store_pk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.store
    ADD CONSTRAINT store_pk PRIMARY KEY (id);


--
-- Name: car_brand_name_uindex; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX car_brand_name_uindex ON public.car_brand USING btree (name);


--
-- Name: car_model_brand_name_uindex; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX car_model_brand_name_uindex ON public.car_model USING btree (brand, name);


--
-- Name: stock_store_car_model_uindex; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX stock_store_car_model_uindex ON public.stock USING btree (store, car_model);


--
-- Name: store_location_uindex; Type: INDEX; Schema: public; Owner: root
--

CREATE UNIQUE INDEX store_location_uindex ON public.store USING btree (location);


--
-- Name: car_model car_model_car_brand_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.car_model
    ADD CONSTRAINT car_model_car_brand_id_fk FOREIGN KEY (brand) REFERENCES public.car_brand(id) ON DELETE RESTRICT;


--
-- Name: stock stock_store_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.stock
    ADD CONSTRAINT stock_store_id_fk FOREIGN KEY (store) REFERENCES public.store(id) ON DELETE RESTRICT;


--
-- PostgreSQL database dump complete
--

