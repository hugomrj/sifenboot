--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3
-- Dumped by pg_dump version 15.3

-- Started on 2025-11-03 19:22:42

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

--
-- TOC entry 5 (class 2615 OID 26660)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 214 (class 1259 OID 26661)
-- Name: _prisma_migrations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public._prisma_migrations (
    id character varying(36) NOT NULL,
    checksum character varying(64) NOT NULL,
    finished_at timestamp with time zone,
    migration_name character varying(255) NOT NULL,
    logs text,
    rolled_back_at timestamp with time zone,
    started_at timestamp with time zone DEFAULT now() NOT NULL,
    applied_steps_count integer DEFAULT 0 NOT NULL
);


ALTER TABLE public._prisma_migrations OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 27941)
-- Name: activity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.activity (
    id text NOT NULL,
    code text NOT NULL,
    description text NOT NULL,
    active boolean DEFAULT true NOT NULL,
    company_id text NOT NULL,
    created_at timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.activity OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 26889)
-- Name: audit_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.audit_log (
    id integer NOT NULL,
    entity text NOT NULL,
    entity_id text,
    action text NOT NULL,
    actor text,
    meta jsonb,
    created_at timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.audit_log OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 26888)
-- Name: audit_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audit_log_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.audit_log_id_seq OWNER TO postgres;

--
-- TOC entry 3761 (class 0 OID 0)
-- Dependencies: 242
-- Name: audit_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.audit_log_id_seq OWNED BY public.audit_log.id;


--
-- TOC entry 216 (class 1259 OID 26680)
-- Name: branch; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.branch (
    id text NOT NULL,
    company_id text NOT NULL,
    code text NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.branch OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 26705)
-- Name: certificate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.certificate (
    id text NOT NULL,
    company_id text NOT NULL,
    alias text NOT NULL,
    storage_uri text NOT NULL,
    passphrase text,
    valid_from timestamp(3) without time zone,
    valid_to timestamp(3) without time zone,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.certificate OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 26670)
-- Name: company; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company (
    id text NOT NULL,
    ruc text NOT NULL,
    email text NOT NULL,
    created_at timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ambiente text NOT NULL,
    csc text NOT NULL,
    csc_id text NOT NULL,
    fecha_test timestamp(3) without time zone,
    fecha_timbrado timestamp(3) without time zone NOT NULL,
    nombre_fantasia text,
    razon_social text NOT NULL,
    record_id text,
    timbrado text NOT NULL,
    timbrado_test text,
    tipo_contribuyente text NOT NULL,
    tipo_regimen text,
    activo boolean,
    address character varying,
    num_casa character varying
);


ALTER TABLE public.company OWNER TO postgres;

--
-- TOC entry 253 (class 1259 OID 36251)
-- Name: company_certificates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_certificates (
    id character varying(36) NOT NULL,
    company_id character varying(36) NOT NULL,
    certificate_alias character varying(100) NOT NULL,
    encrypted_certificate text NOT NULL,
    certificate_password text NOT NULL,
    active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    expires_at timestamp without time zone
);


ALTER TABLE public.company_certificates OWNER TO postgres;

--
-- TOC entry 254 (class 1259 OID 36262)
-- Name: company_timbrados; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company_timbrados (
    id character varying(36) NOT NULL,
    company_id character varying(36) NOT NULL,
    timbrado_number character varying(20) NOT NULL,
    valid_from timestamp without time zone NOT NULL,
    valid_to timestamp without time zone NOT NULL,
    active boolean DEFAULT true,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.company_timbrados OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 26714)
-- Name: csc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.csc (
    id text NOT NULL,
    company_id text NOT NULL,
    code text NOT NULL,
    label text,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.csc OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 26750)
-- Name: customer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer (
    id text NOT NULL,
    company_id text NOT NULL,
    doc_type text DEFAULT 'RUC'::text NOT NULL,
    doc_number text NOT NULL,
    name text NOT NULL,
    email text,
    phone text,
    address text,
    num_casa integer,
    diplomatico boolean DEFAULT false NOT NULL,
    dncp integer DEFAULT 0 NOT NULL,
    pais_receptor_id text,
    tipo_documento_identidad_id text
);


ALTER TABLE public.customer OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 26881)
-- Name: denominacion_tarjeta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.denominacion_tarjeta (
    codigo text NOT NULL,
    descripcion text NOT NULL
);


ALTER TABLE public.denominacion_tarjeta OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 26760)
-- Name: doc_seq; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.doc_seq (
    id text NOT NULL,
    company_id text NOT NULL,
    timbrado_id text NOT NULL,
    branch_id text NOT NULL,
    pos_id text NOT NULL,
    doc_type text DEFAULT 'FE'::text NOT NULL,
    current_number integer DEFAULT 0 NOT NULL,
    last_updated timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.doc_seq OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 36276)
-- Name: document_customers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.document_customers (
    id character varying(36) NOT NULL,
    document_id character varying(36) NOT NULL,
    ruc character varying(30) NOT NULL,
    nombre text NOT NULL,
    direccion text,
    cpais character varying(3),
    correo character varying(255),
    num_casa integer DEFAULT 0 NOT NULL,
    diplomatico boolean DEFAULT false NOT NULL,
    dncp integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    doc_type character varying(10),
    doc_number character varying(30),
    tipo_documento_identidad_id character varying(2)
);


ALTER TABLE public.document_customers OWNER TO postgres;

--
-- TOC entry 250 (class 1259 OID 36213)
-- Name: document_events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.document_events (
    id character varying(36) NOT NULL,
    document_id character varying(36) NOT NULL,
    event_type character varying(50) NOT NULL,
    event_data text,
    metadata jsonb,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    user_id character varying(36),
    reason text
);


ALTER TABLE public.document_events OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 36189)
-- Name: document_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.document_items (
    id character varying(36) NOT NULL,
    document_id character varying(36) NOT NULL,
    item_code character varying(100),
    description text NOT NULL,
    quantity numeric(10,2) NOT NULL,
    unit_price numeric(15,2) NOT NULL,
    line_total numeric(15,2) NOT NULL,
    iva_rate numeric(5,2),
    iva_amount numeric(15,2),
    unit_measure_code character varying(10),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    base_grav_amount numeric(18,2),
    tipo_iva_code character varying(2)
);


ALTER TABLE public.document_items OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 36202)
-- Name: document_payments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.document_payments (
    id character varying(36) NOT NULL,
    document_id character varying(36) NOT NULL,
    payment_method_code character varying(10) NOT NULL,
    payment_method_name character varying(100) NOT NULL,
    amount numeric(15,2) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    currency_code character varying(3),
    exchange_rate numeric(18,6) DEFAULT 0,
    card_brand_code character varying(10),
    card_last4 character varying(4),
    auth_code character varying(32),
    pos_reference character varying(64)
);


ALTER TABLE public.document_payments OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 36238)
-- Name: document_signatures; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.document_signatures (
    id character varying(36) NOT NULL,
    company_id character varying(36) NOT NULL,
    document_id character varying(36),
    xml_hash character varying(255) NOT NULL,
    certificate_alias character varying(100) NOT NULL,
    signature_algorithm character varying(50) NOT NULL,
    signature_value text,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.document_signatures OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 36175)
-- Name: documents; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.documents (
    id character varying(36) NOT NULL,
    company_id character varying(36) NOT NULL,
    doc_type character varying(10) NOT NULL,
    series character varying(10) NOT NULL,
    number integer NOT NULL,
    status character varying(20) DEFAULT 'RECEIVED'::character varying NOT NULL,
    idempotency_key character varying(255) NOT NULL,
    track_id character varying(255) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    last_state_change timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    last_event character varying(50),
    cdc character varying(255),
    qr_link text,
    xml_link text,
    last_error text,
    issue_datetime timestamp without time zone,
    currency_code character varying(3),
    exchange_rate numeric(18,6) DEFAULT 0,
    total_exenta numeric(18,2) DEFAULT 0 NOT NULL,
    total_grav5 numeric(18,2) DEFAULT 0 NOT NULL,
    total_grav10 numeric(18,2) DEFAULT 0 NOT NULL,
    total_tax5 numeric(18,2) DEFAULT 0 NOT NULL,
    total_tax10 numeric(18,2) DEFAULT 0 NOT NULL,
    total_amount numeric(18,2),
    tipo_documento character varying(2),
    tipo_emision integer,
    tipo_transaccion character varying(2),
    condicion_pago integer,
    codigo_seguridad_aleatorio character varying(9),
    total_pago numeric(18,2),
    total_redondeo numeric(18,2) DEFAULT 0 NOT NULL,
    establecimiento character(3),
    punto character(3),
    timbrado_number character varying(20),
    descripcion text,
    xml_signed_hash character varying(128),
    xml_signature_value text,
    signed_at timestamp without time zone,
    numero character varying
);


ALTER TABLE public.documents OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 36166)
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 26770)
-- Name: invoice; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoice (
    id text NOT NULL,
    company_id text NOT NULL,
    customer_id text,
    timbrado_id text NOT NULL,
    branch_id text NOT NULL,
    pos_id text NOT NULL,
    doc_type text DEFAULT 'FE'::text NOT NULL,
    series text NOT NULL,
    number integer NOT NULL,
    issue_datetime timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    currency text DEFAULT 'PYG'::text NOT NULL,
    total_exenta double precision DEFAULT 0 NOT NULL,
    total_grav5 double precision DEFAULT 0 NOT NULL,
    total_grav10 double precision DEFAULT 0 NOT NULL,
    total_tax5 double precision DEFAULT 0 NOT NULL,
    total_tax10 double precision DEFAULT 0 NOT NULL,
    total_amount double precision NOT NULL,
    status text DEFAULT 'DRAFT'::text NOT NULL,
    cdc text,
    cdc_space text,
    qr_link text,
    xml_link text,
    kude_link text,
    receipt_id text,
    descripcion text,
    tipo_documento_id text,
    tipo_emision integer DEFAULT 1 NOT NULL,
    tipo_transaccion_id text,
    tipo_operacion_id text,
    condicion_pago integer DEFAULT 1 NOT NULL,
    moneda_id text,
    cambio double precision DEFAULT 0 NOT NULL,
    codigo_seguridad_aleatorio text,
    total_pago double precision,
    total_redondeo double precision DEFAULT 0 NOT NULL,
    motivo_emision_id text,
    created_at timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_error text,
    updated_at timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    xml_hash text
);


ALTER TABLE public.invoice OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 26790)
-- Name: invoice_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoice_item (
    id text NOT NULL,
    invoice_id text NOT NULL,
    product_id text,
    codigo text,
    description text NOT NULL,
    qty double precision NOT NULL,
    unit_price double precision NOT NULL,
    tax_id integer,
    line_total double precision NOT NULL,
    unidad_medida_id text,
    tipo_iva_id text,
    iva_tasa double precision,
    base_grav_item double precision,
    liq_iva_item double precision
);


ALTER TABLE public.invoice_item OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 26797)
-- Name: invoice_payment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoice_payment (
    id text NOT NULL,
    invoice_id text NOT NULL,
    metodo_pago_id text NOT NULL,
    denominacion_tarjeta_id text,
    monto double precision NOT NULL,
    descripcion_tarjeta text
);


ALTER TABLE public.invoice_payment OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 26860)
-- Name: metodo_pago; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.metodo_pago (
    codigo text NOT NULL,
    metodo text NOT NULL
);


ALTER TABLE public.metodo_pago OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 26825)
-- Name: moneda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.moneda (
    "codigoISO" text NOT NULL,
    moneda text NOT NULL,
    simbolo text NOT NULL
);


ALTER TABLE public.moneda OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 26867)
-- Name: motivo_emision; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.motivo_emision (
    codigo text NOT NULL,
    motivo text NOT NULL
);


ALTER TABLE public.motivo_emision OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 27951)
-- Name: obligation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.obligation (
    id text NOT NULL,
    code text NOT NULL,
    description text NOT NULL,
    active boolean DEFAULT true NOT NULL,
    company_id text NOT NULL,
    created_at timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.obligation OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 36226)
-- Name: outbox_events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.outbox_events (
    id character varying(36) NOT NULL,
    aggregate_id character varying(36) NOT NULL,
    aggregate_type character varying(50) NOT NULL,
    event_type character varying(50) NOT NULL,
    event_data text,
    metadata jsonb,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    processed_at timestamp without time zone,
    status character varying(20) DEFAULT 'PENDING'::character varying NOT NULL,
    retry_count integer DEFAULT 0,
    last_error text,
    idempotency_key character varying(255) NOT NULL,
    company_id character varying(36) NOT NULL,
    track_id character varying(255) NOT NULL,
    payload jsonb
);


ALTER TABLE public.outbox_events OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 26832)
-- Name: pais_receptor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pais_receptor (
    "codigoISO3" text NOT NULL,
    pais text NOT NULL
);


ALTER TABLE public.pais_receptor OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 26687)
-- Name: pos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pos (
    id text NOT NULL,
    branch_id text NOT NULL,
    code text NOT NULL,
    description text,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.pos OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 26740)
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id text NOT NULL,
    company_id text NOT NULL,
    sku text,
    name text NOT NULL,
    unit text DEFAULT 'UNI'::text NOT NULL,
    price double precision DEFAULT 0 NOT NULL,
    tax_id integer,
    active boolean DEFAULT true NOT NULL,
    unidad_medida_id text,
    tipo_iva_id text
);


ALTER TABLE public.product OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 26874)
-- Name: responsable_emision; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.responsable_emision (
    codigo text NOT NULL,
    descripcion text NOT NULL
);


ALTER TABLE public.responsable_emision OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 26732)
-- Name: tax; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tax (
    id integer NOT NULL,
    code text NOT NULL,
    rate double precision NOT NULL,
    description text
);


ALTER TABLE public.tax OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 26731)
-- Name: tax_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tax_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tax_id_seq OWNER TO postgres;

--
-- TOC entry 3762 (class 0 OID 0)
-- Dependencies: 222
-- Name: tax_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tax_id_seq OWNED BY public.tax.id;


--
-- TOC entry 221 (class 1259 OID 26723)
-- Name: timbrado; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.timbrado (
    id text NOT NULL,
    company_id text NOT NULL,
    number text NOT NULL,
    valid_from timestamp(3) without time zone NOT NULL,
    valid_to timestamp(3) without time zone NOT NULL,
    branch_id text,
    pos_id text,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.timbrado OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 26804)
-- Name: tipo_documento_electronico; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_documento_electronico (
    codigo text NOT NULL,
    descripcion text NOT NULL
);


ALTER TABLE public.tipo_documento_electronico OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 26839)
-- Name: tipo_documento_identidad; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_documento_identidad (
    codigo text NOT NULL,
    descripcion text NOT NULL
);


ALTER TABLE public.tipo_documento_identidad OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 26853)
-- Name: tipo_iva; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_iva (
    codigo text NOT NULL,
    descripcion text NOT NULL
);


ALTER TABLE public.tipo_iva OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 26818)
-- Name: tipo_operacion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_operacion (
    codigo text NOT NULL,
    descripcion text NOT NULL
);


ALTER TABLE public.tipo_operacion OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 26811)
-- Name: tipo_transaccion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo_transaccion (
    codigo text NOT NULL,
    descripcion text NOT NULL
);


ALTER TABLE public.tipo_transaccion OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 26846)
-- Name: unidad_medida; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.unidad_medida (
    codigo text NOT NULL,
    unidad text NOT NULL
);


ALTER TABLE public.unidad_medida OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 26695)
-- Name: user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."user" (
    id text NOT NULL,
    company_id text NOT NULL,
    email text NOT NULL,
    full_name text NOT NULL,
    password_hash text NOT NULL,
    role text DEFAULT 'admin'::text NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp(3) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    refresh_token text
);


ALTER TABLE public."user" OWNER TO postgres;

--
-- TOC entry 3368 (class 2604 OID 26892)
-- Name: audit_log id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit_log ALTER COLUMN id SET DEFAULT nextval('public.audit_log_id_seq'::regclass);


--
-- TOC entry 3343 (class 2604 OID 26735)
-- Name: tax id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tax ALTER COLUMN id SET DEFAULT nextval('public.tax_id_seq'::regclass);


--
-- TOC entry 3713 (class 0 OID 26661)
-- Dependencies: 214
-- Data for Name: _prisma_migrations; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public._prisma_migrations VALUES ('032f6212-ad37-4c02-9ca2-66bcc17cb30e', 'f34b64ff1ee539656cfd24566b64a9b4ee0d7853e633b8d96622f2b52adeb945', '2025-10-06 22:26:58.626337-03', '20251002175955_init_postgresql_sifen', NULL, NULL, '2025-10-06 22:26:57.550407-03', 1);
INSERT INTO public._prisma_migrations VALUES ('972a381e-296f-4ae3-a3cc-1e7300af02a0', 'c204c3bc18e14d8653867221fee95ac4df29de6297d31448449cebb730d9353c', '2025-10-06 22:26:58.630725-03', '20251002194728_add_refresh_token', NULL, NULL, '2025-10-06 22:26:58.627428-03', 1);
INSERT INTO public._prisma_migrations VALUES ('d926905b-cd83-4883-95c8-bb3966f58c0a', 'c934bbc6e41c3a99ea631b4b6465c6f25365315f7cf2b209e0bddebdd87d7136', '2025-10-06 22:27:37.158795-03', '20251007012737_add_record_id_to_company', NULL, NULL, '2025-10-06 22:27:37.058168-03', 1);
INSERT INTO public._prisma_migrations VALUES ('6aef75fe-422a-4d42-be8e-967402823e8d', '5b3b67fb00d90efcbd17f0c988f5bc9ca8d5f65452855a1ce585fe9911440d29', '2025-10-11 22:14:00.922741-03', '20251012011400_add_last_error_xml_hash_timestamps_to_invoice', NULL, NULL, '2025-10-11 22:14:00.874529-03', 1);


--
-- TOC entry 3743 (class 0 OID 27941)
-- Dependencies: 244
-- Data for Name: activity; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.activity VALUES ('cmgfvw23f0001j2cgmrpb5wc6', '6201', 'Servicios de desarrollo de software', true, '2a056255-15d3-4bcc-ad90-1b18f5037874', '2025-10-07 01:31:23.931', '2025-10-07 01:31:23.931');
INSERT INTO public.activity VALUES ('cmgfvw23u0003j2cgcqy51l44', '6202', 'Servicios de consultoría en informática', true, '2a056255-15d3-4bcc-ad90-1b18f5037874', '2025-10-07 01:31:23.945', '2025-10-07 01:31:23.945');
INSERT INTO public.activity VALUES ('cmgfxp03c0001j2toer9l7m76', '1234', 'Comercio minorista', true, '9b35d76d-a0eb-43b7-90fb-80740cef9217', '2025-10-07 02:21:53.875', '2025-10-07 02:21:53.875');
INSERT INTO public.activity VALUES ('cmgfxubpa0003j2tojs62p1t1', '11111', 'Comercio Minorista', true, '9ff0a481-ca48-460c-9be2-016ae3af10a9', '2025-10-07 02:26:02.301', '2025-10-07 02:26:02.301');
INSERT INTO public.activity VALUES ('cmgfy3vmf0001j2mkteueg3ew', '0000', 'pOR MI', true, 'a2880d1b-05d6-415c-a65d-a71b26861b09', '2025-10-07 02:33:28.023', '2025-10-07 02:33:28.023');
INSERT INTO public.activity VALUES ('cmgfydvpi0003j2mk08g0xw17', '213', 'sssss', true, '37bcadac-636b-45a4-ae2b-cebedff50a45', '2025-10-07 02:41:14.695', '2025-10-07 02:41:14.695');
INSERT INTO public.activity VALUES ('cmgfyjdt30005j2mkoa3w70nr', '123456', '323232', true, 'd2611d26-a8be-4bfe-9c2e-adc144f7cd81', '2025-10-07 02:45:31.43', '2025-10-07 02:45:31.43');
INSERT INTO public.activity VALUES ('cmgfyp2hq0007j2mkfr65w4kg', '25', 'Jorge', true, '29812b30-b416-4517-8ff0-c1a7ae76dc42', '2025-10-07 02:49:56.702', '2025-10-07 02:49:56.702');
INSERT INTO public.activity VALUES ('cmgfys9lz0009j2mkkslgniqv', '123', 'Jugar', true, '487480c2-4e41-40a8-8a13-e3d5bbfb51c5', '2025-10-07 02:52:25.894', '2025-10-07 02:52:25.894');
INSERT INTO public.activity VALUES ('cmgfz095o000bj2mkdsj73yww', '9876', 'Saborear', true, 'e606afd2-4e14-4fac-a236-25fe6fd8a264', '2025-10-07 02:58:38.555', '2025-10-07 02:58:38.555');
INSERT INTO public.activity VALUES ('cmgfznhpx000dj2mkygt7q1cp', '12', 'Cara vives', true, '07ca9fc4-f4da-4c4a-a578-eb863770e8ea', '2025-10-07 03:16:42.741', '2025-10-07 03:16:42.741');
INSERT INTO public.activity VALUES ('cmgg0282w0001j2ispxlcekk5', '4321', 'Amas', true, '47e4fee1-42b3-4fac-bc73-fd68730dff90', '2025-10-07 03:28:10.087', '2025-10-07 03:28:10.087');
INSERT INTO public.activity VALUES ('cmgg2mnct0001j2q829fjqb9s', '5678', 'mismas', true, 'b1b62708-5e61-421e-a440-94fa8b83d558', '2025-10-07 04:40:02.232', '2025-10-07 04:40:02.232');
INSERT INTO public.activity VALUES ('cmgg418cy0001j2rchiuixy5c', '43', 'lklklk', true, '2af8e083-8eb6-41c2-abfa-d7d6ee4ab6e1', '2025-10-07 05:19:22.252', '2025-10-07 05:19:22.252');
INSERT INTO public.activity VALUES ('cmgg4bgae0001j2kk2463xdgt', '6789', 'ghjk', true, '821a7281-cc67-4e0b-8503-189900d98c0d', '2025-10-07 05:27:19.094', '2025-10-07 05:27:19.094');
INSERT INTO public.activity VALUES ('cmgg4wrci0001j2bkupgme4qh', 'as', 'as', true, 'a6f80e29-af97-4f37-802b-21157e10b46b', '2025-10-07 05:43:53.202', '2025-10-07 05:43:53.202');
INSERT INTO public.activity VALUES ('cmgg5d9l50001j2lcu33dtprc', 'm', 'yuio', true, '7788ac61-e7cc-4227-95b7-5c8351370ffc', '2025-10-07 05:56:43.337', '2025-10-07 05:56:43.337');
INSERT INTO public.activity VALUES ('cmgh5lbib0001j2v0akl6grwp', '2345r', '234ret', true, 'f8d91ca1-df4a-4a4a-a0fc-ef97c0981e9e', '2025-10-07 22:50:45.247', '2025-10-07 22:50:45.247');


--
-- TOC entry 3742 (class 0 OID 26889)
-- Dependencies: 243
-- Data for Name: audit_log; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3715 (class 0 OID 26680)
-- Dependencies: 216
-- Data for Name: branch; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.branch VALUES ('ed13dfa8-fa1a-43f6-93c8-efdb3e4a5c19', '2a056255-15d3-4bcc-ad90-1b18f5037874', '001', 'Casa Matriz');
INSERT INTO public.branch VALUES ('BRANCH1', 'COMPANY1', '001', 'Matriz');


--
-- TOC entry 3718 (class 0 OID 26705)
-- Dependencies: 219
-- Data for Name: certificate; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.certificate VALUES ('CERT1', 'COMPANY1', 'dummy', '/path/to/dummy.p12', 'password', '2023-01-01 00:00:00', '2027-12-31 00:00:00', true, '2025-10-19 21:59:42.816');


--
-- TOC entry 3714 (class 0 OID 26670)
-- Dependencies: 215
-- Data for Name: company; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.company VALUES ('2a056255-15d3-4bcc-ad90-1b18f5037874', '80000001-7', 'demo@empresa.com.py', '2025-10-07 01:31:23.841', '2025-10-07 01:31:23.841', 'PRODUCCION', 'ABCD0000000000000000000000000000', '0001', NULL, '2024-01-10 00:00:00', 'Demo Company', 'Empresa Demo S.A.', NULL, '1234567', NULL, 'PERSONA_JURIDICA', 'TURISMO', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('4304c2bc-ca54-4ceb-ba20-702a1a5205cf', '8000000', 'l@l.com', '2025-10-07 01:51:15.241', '2025-10-07 01:51:15.241', '1', 'teetwetet', '123456', NULL, '2025-09-30 00:00:00', 'Jorge', 'Jorge Ramirez', '', '1234567', '', '2', '3', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('9b35d76d-a0eb-43b7-90fb-80740cef9217', '10101010', 'hola@example.com', '2025-10-07 01:57:05.932', '2025-10-07 01:57:05.932', '1', 'Demente', '12345', NULL, '2025-09-30 00:00:00', 'Batman', 'Batman', '', '1234567', '', '2', '2', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('9ff0a481-ca48-460c-9be2-016ae3af10a9', '111111111', 'leketo@leketo.com', '2025-10-07 02:25:43.702', '2025-10-07 02:25:43.702', '1', 'aaaaaaaaaaaaaaaa', '111111', NULL, '2025-09-29 00:00:00', 'qqqqqqqqq', 'qqqqqqqq', '', '1111111', '', '2', '1', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('a2880d1b-05d6-415c-a65d-a71b26861b09', '2222222222', 'leketo@lekro.com', '2025-10-07 02:32:49.566', '2025-10-07 02:32:49.566', '1', 'dasssdassdadsa', '1234', NULL, '2000-01-01 00:00:00', 'wwwwwwwwwwwww', 'aaaaaaaaaaaaaaa', '', '1111111', '', '2', '2', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('37bcadac-636b-45a4-ae2b-cebedff50a45', '1111111112', 'L@l.com', '2025-10-07 02:41:05.61', '2025-10-07 02:41:05.61', '1', 'lkkkkkkkk', '12344', NULL, '2025-09-30 00:00:00', 'SSSSSSSSS', 'QQQQQQQQQQQ', '', '1111111', '', '2', '3', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('d2611d26-a8be-4bfe-9c2e-adc144f7cd81', '93399393', 'leketo@leketo.com', '2025-10-07 02:45:05.532', '2025-10-07 02:45:05.532', '1', 'dSDDSADSASD', '12345', NULL, '2025-09-30 00:00:00', 'Dientes', 'Dientes', '', '8558782', '', '2', '2', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('29812b30-b416-4517-8ff0-c1a7ae76dc42', '1111111113', 'eje@eje.com', '2025-10-07 02:49:42.816', '2025-10-07 02:49:42.816', '1', 'asssssssssss', '111111', NULL, '2025-09-30 00:00:00', 'aaaaaaaaaaaa', 'aaaaaaaa', '', '1111111', '', '2', '1', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('487480c2-4e41-40a8-8a13-e3d5bbfb51c5', '1098765', 'lekrto@lek.com', '2025-10-07 02:52:06.626', '2025-10-07 02:52:06.626', '1', 'ASFG', '12345', NULL, '2025-09-29 00:00:00', 'NNNNNNNNNNN', 'nnnnnnnnN', '', '1111111', '', '2', '1', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('e606afd2-4e14-4fac-a236-25fe6fd8a264', '16111711', 'l@l.com', '2025-10-07 02:58:13.857', '2025-10-07 02:58:13.857', '1', 'fghjkjhgfd', '12345', NULL, '2025-09-30 00:00:00', 'Ok', 'Ok', '', '1111111', '', '2', '1', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('07ca9fc4-f4da-4c4a-a578-eb863770e8ea', '45678939', 'leketo@leketo.com', '2025-10-07 03:16:28.482', '2025-10-07 03:16:28.482', '1', 'adfmiIIIIII', '12345', NULL, '2025-09-29 00:00:00', 'Miedo', 'Miedo', '', '1234567', '', '2', '3', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('47e4fee1-42b3-4fac-bc73-fd68730dff90', '88858585', 'jo@jo.com', '2025-10-07 03:27:27.95', '2025-10-07 03:27:27.95', '1', 'asdfg', '12345', NULL, '2025-09-29 00:00:00', 'Ciru', 'Ciru', '', '9876789', '', '2', '1', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('87a3dce4-08d9-4f2d-8569-463e32dea8bb', '1234567', 'Saquen@darme.com', '2025-10-07 03:42:27.032', '2025-10-07 03:42:27.032', '1', 'asdf12344', '123345', NULL, '2025-09-29 00:00:00', 'Familiar', 'Familiar', '', '1234321', '', '2', '2', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('b1b62708-5e61-421e-a440-94fa8b83d558', '8888888', 'lol@lol.com', '2025-10-07 04:39:49.699', '2025-10-07 04:39:49.699', '1', 'lllkllklklklk', '567890', NULL, '2025-09-30 00:00:00', 'asdasdssa', 'asddas', '', '6789098', '', '2', '3', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('2af8e083-8eb6-41c2-abfa-d7d6ee4ab6e1', '22222333', 'l@l.c', '2025-10-07 05:19:16.022', '2025-10-07 05:19:16.022', '1', 'adgfhdsjak', '678', NULL, '2025-09-29 00:00:00', 'wqwe', 'ewewewe', '', '4567892', '', '2', '2', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('821a7281-cc67-4e0b-8503-189900d98c0d', '6565656556', 'l@l.com', '2025-10-07 05:27:11.158', '2025-10-07 05:27:11.158', '1', 'yuhjn', '1234', NULL, '2025-09-29 00:00:00', 'tfgyhjkl', 'tyuiop', '', '1234500', '', '2', '1', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('683f9edb-789a-4fe6-8a21-52fd89666f42', '65656565564', 'l@l.com', '2025-10-07 05:42:28.119', '2025-10-07 05:42:28.119', '1', 'yuhjn', '1234', NULL, '2025-09-29 00:00:00', 'tfgyhjkl', 'tyuiop', '', '1234500', '', '2', '1', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('a6f80e29-af97-4f37-802b-21157e10b46b', '333333322', 'leketo@gmail.com', '2025-10-07 05:43:47.55', '2025-10-07 05:43:47.55', '1', 'sadssa', 'sasdaas', NULL, '2025-09-30 00:00:00', 'sasa', 'dsC', '', '4434344', '', '2', '2', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('7788ac61-e7cc-4227-95b7-5c8351370ffc', '987656789', 'l@l.com', '2025-10-07 05:56:37.313', '2025-10-07 05:56:37.313', '1', 'dfghj', '5678', NULL, '2025-09-29 00:00:00', 'l;l;l;ll;;l', 'fghjk', '', '0987654', '', '2', '4', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('f8d91ca1-df4a-4a4a-a0fc-ef97c0981e9e', '4567890', 'a@a.com', '2025-10-07 22:50:09.029', '2025-10-07 22:50:09.029', '1', 'lkjflksdjlf', '1234', NULL, '2025-09-29 00:00:00', 'wesdsaddsa', 'ertyuio', '', '2345678', '', '2', '2', NULL, NULL, NULL);
INSERT INTO public.company VALUES ('48c16c8f-a221-4c32-bd14-9f3bbe6a55ad', '80012345-6', 'nbvbnm@gmail.com', '2025-10-12 03:45:52.639', '2025-10-12 03:45:52.639', 'PRODUCCION', 'ABCD0000000000000000000000000000', '0001', NULL, '2025-10-24 00:00:00', '3232333', '3233232332', NULL, '1234567', '', 'PERSONA_JURIDICA', 'TURISMO', true, NULL, NULL);
INSERT INTO public.company VALUES ('COMPANY1', '80000001-3', 'empresa@test.com', '2025-10-19 21:36:34.8', '2025-10-19 21:36:34.8', 'test', 'CODE1234', 'CSC001', NULL, '2024-01-01 00:00:00', 'Mi Empresa', 'Mi Empresa S.R.L.', NULL, '12345678', '87654321', 'Contribuyente', 'Regimen General', true, 'Canada San Rafael', '0987654567');


--
-- TOC entry 3752 (class 0 OID 36251)
-- Dependencies: 253
-- Data for Name: company_certificates; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.company_certificates VALUES ('CCERT1', 'COMPANY1', 'dummy', 'dummy_encrypted', 'encrypted_password', true, '2025-10-19 21:59:48.237934', NULL);


--
-- TOC entry 3753 (class 0 OID 36262)
-- Dependencies: 254
-- Data for Name: company_timbrados; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.company_timbrados VALUES ('CTIMB1', 'COMPANY1', '12345678', '2024-01-01 00:00:00', '2025-12-31 00:00:00', true, '2025-10-19 21:45:14.365137');


--
-- TOC entry 3719 (class 0 OID 26714)
-- Dependencies: 220
-- Data for Name: csc; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.csc VALUES ('CSC001', 'COMPANY1', 'CODE1234', 'Control Code Test', true, '2025-10-19 21:37:06.943');


--
-- TOC entry 3724 (class 0 OID 26750)
-- Dependencies: 225
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3740 (class 0 OID 26881)
-- Dependencies: 241
-- Data for Name: denominacion_tarjeta; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.denominacion_tarjeta VALUES ('1', 'Visa');
INSERT INTO public.denominacion_tarjeta VALUES ('2', 'Mastercard');
INSERT INTO public.denominacion_tarjeta VALUES ('3', 'American Express');
INSERT INTO public.denominacion_tarjeta VALUES ('4', 'Maestro');
INSERT INTO public.denominacion_tarjeta VALUES ('5', 'Panal');
INSERT INTO public.denominacion_tarjeta VALUES ('6', 'Cabal');
INSERT INTO public.denominacion_tarjeta VALUES ('99', 'Otra denominación de tarjeta (descripción requerida)');


--
-- TOC entry 3725 (class 0 OID 26760)
-- Dependencies: 226
-- Data for Name: doc_seq; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3754 (class 0 OID 36276)
-- Dependencies: 255
-- Data for Name: document_customers; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.document_customers VALUES ('95e5ff75-be57-42e3-87ff-8b6490fcba23', '094b7e39-8e93-4f2c-9f8e-ed28bb7b46fe', '80000002-6', 'Cliente Prueba', 'Av. Principal 123', 'PRY', 'cliente@test.com', 0, false, 0, '2025-10-24 02:18:39.332834', NULL, NULL, NULL);
INSERT INTO public.document_customers VALUES ('98e73fdb-35dc-4edc-ade9-498ce5053681', '137a470f-e20b-466c-83ed-e4031968855a', '80000002-6', 'Cliente Prueba S.A.', 'Av. Principal 123', 'PRY', 'cliente@test.com', 0, false, 0, '2025-10-26 23:28:48.895283', NULL, NULL, NULL);
INSERT INTO public.document_customers VALUES ('6fb2eca6-bed0-45c5-846c-38ea0339a0bc', '7d752b71-52c6-410b-aa88-6066fa2578d7', '80000002-6', 'Cliente Prueba S.A.', 'Av. Principal 123', 'PRY', 'cliente@test.com', 0, false, 0, '2025-10-27 00:18:46.484125', NULL, NULL, NULL);
INSERT INTO public.document_customers VALUES ('821ff90a-0f83-4d3c-bbcc-59f93250b18b', '81b343c9-5374-4bab-b586-38f9af2c1683', '80000002-6', 'Cliente Prueba S.A.', 'Av. Principal 123', 'PRY', 'cliente@test.com', 0, false, 0, '2025-10-28 12:44:08.760974', NULL, NULL, NULL);
INSERT INTO public.document_customers VALUES ('ccc31a52-69dc-4ebe-a29a-cfcd657fa49a', '6ef4f090-89ff-4675-954c-1eb506552151', '80000002-6', 'Cliente Prueba S.A.', 'Av. Principal 123', 'PRY', 'cliente@test.com', 0, false, 0, '2025-10-30 14:15:01.829696', NULL, NULL, NULL);
INSERT INTO public.document_customers VALUES ('322bc58c-0c8f-471f-a4ed-02eb2e2b5027', '7aebdd10-5b31-4bd2-be8d-a9037a49ec38', '80000002-6', 'Cliente Prueba S.A.', 'Av. Principal 123', 'PRY', 'cliente@test.com', 0, false, 0, '2025-10-30 16:33:52.637569', NULL, NULL, NULL);
INSERT INTO public.document_customers VALUES ('26697dd3-6ce7-4443-a2d8-2b8ad7211a6c', '7434864f-eb6c-4550-af10-adac152ba50d', '80000002-6', 'Cliente Prueba S.A.', 'Av. Principal 123', 'PRY', 'cliente@test.com', 0, false, 0, '2025-10-30 22:03:03.504957', NULL, NULL, NULL);


--
-- TOC entry 3749 (class 0 OID 36213)
-- Dependencies: 250
-- Data for Name: document_events; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3747 (class 0 OID 36189)
-- Dependencies: 248
-- Data for Name: document_items; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.document_items VALUES ('5d62c8df-6b50-4912-b673-5f33d5a08991', '094b7e39-8e93-4f2c-9f8e-ed28bb7b46fe', 'P001', 'Producto de prueba', 1.00, 10000.00, 10000.00, 10.00, 910.00, '77', '2025-10-24 02:18:39.357113', NULL, NULL);
INSERT INTO public.document_items VALUES ('db91f8a4-9ab7-4940-9fe1-20582ab3c657', '137a470f-e20b-466c-83ed-e4031968855a', 'RAD-TOY-H18', 'RADIADOR TOYOTA HILUX 2018', 1.00, 430000.00, 430000.00, 10.00, 39091.00, '77', '2025-10-26 23:29:14.786608', 390909.00, '1');
INSERT INTO public.document_items VALUES ('9922f4ac-6b9e-4c8c-ad96-ee0e9df35960', '137a470f-e20b-466c-83ed-e4031968855a', 'PARA-TOY-H18', 'PARAGOLPE TRASERO HILUX 2018', 1.00, 550000.00, 550000.00, 10.00, 50000.00, '77', '2025-10-26 23:29:14.79877', 500000.00, '1');
INSERT INTO public.document_items VALUES ('9747253e-809d-4126-b12a-3dfa2f84913c', '7d752b71-52c6-410b-aa88-6066fa2578d7', 'RAD-TOY-H18', 'RADIADOR TOYOTA HILUX 2018', 1.00, 430000.00, 430000.00, 10.00, 39091.00, '77', '2025-10-27 00:18:51.656214', 390909.00, '1');
INSERT INTO public.document_items VALUES ('68512b94-c153-480c-afde-c227d6bfffef', '7d752b71-52c6-410b-aa88-6066fa2578d7', 'PARA-TOY-H18', 'PARAGOLPE TRASERO HILUX 2018', 1.00, 550000.00, 550000.00, 10.00, 50000.00, '77', '2025-10-27 00:18:51.673523', 500000.00, '1');
INSERT INTO public.document_items VALUES ('c4a09528-8295-4ef4-a603-a9e242007978', '81b343c9-5374-4bab-b586-38f9af2c1683', 'RAD-TOY-H18', 'RADIADOR TOYOTA HILUX 2018', 1.00, 430000.00, 430000.00, 10.00, 39091.00, '77', '2025-10-28 12:44:16.292991', 390909.00, '1');
INSERT INTO public.document_items VALUES ('30edd11a-d7f2-4f62-9b16-154ccca7cea6', '81b343c9-5374-4bab-b586-38f9af2c1683', 'PARA-TOY-H18', 'PARAGOLPE TRASERO HILUX 2018', 1.00, 550000.00, 550000.00, 10.00, 50000.00, '77', '2025-10-28 12:44:16.40267', 500000.00, '1');
INSERT INTO public.document_items VALUES ('97e981f8-7c1c-4a34-80df-5fa79395ed4b', '6ef4f090-89ff-4675-954c-1eb506552151', 'RAD-TOY-H18', 'RADIADOR TOYOTA HILUX 2018', 1.00, 430000.00, 430000.00, 10.00, 39091.00, '77', '2025-10-30 14:15:01.837561', 390909.00, '1');
INSERT INTO public.document_items VALUES ('4b8ea2ae-d4bd-4931-b467-bcaa2411550a', '6ef4f090-89ff-4675-954c-1eb506552151', 'PARA-TOY-H18', 'PARAGOLPE TRASERO HILUX 2018', 1.00, 550000.00, 550000.00, 10.00, 50000.00, '77', '2025-10-30 14:15:01.848132', 500000.00, '1');
INSERT INTO public.document_items VALUES ('3c73cac8-7971-4926-a491-17713851e119', '7aebdd10-5b31-4bd2-be8d-a9037a49ec38', 'RAD-TOY-H18', 'RADIADOR TOYOTA HILUX 2018', 1.00, 430000.00, 430000.00, 10.00, 39091.00, '77', '2025-10-30 16:37:21.618898', 390909.00, '1');
INSERT INTO public.document_items VALUES ('00f277c4-7c53-4e7a-8e9d-e363b124747c', '7aebdd10-5b31-4bd2-be8d-a9037a49ec38', 'PARA-TOY-H18', 'PARAGOLPE TRASERO HILUX 2018', 1.00, 550000.00, 550000.00, 10.00, 50000.00, '77', '2025-10-30 16:37:21.645428', 500000.00, '1');
INSERT INTO public.document_items VALUES ('5c2c528f-1fd2-409c-b958-9b5c16680d72', '7434864f-eb6c-4550-af10-adac152ba50d', 'RAD-TOY-H18', 'RADIADOR TOYOTA HILUX 2018', 1.00, 430000.00, 430000.00, 10.00, 39091.00, '77', '2025-10-30 22:03:03.55108', 390909.00, '1');
INSERT INTO public.document_items VALUES ('238a3095-9ac4-4854-8d13-508171285dfd', '7434864f-eb6c-4550-af10-adac152ba50d', 'PARA-TOY-H18', 'PARAGOLPE TRASERO HILUX 2018', 1.00, 550000.00, 550000.00, 10.00, 50000.00, '77', '2025-10-30 22:03:03.558071', 500000.00, '1');


--
-- TOC entry 3748 (class 0 OID 36202)
-- Dependencies: 249
-- Data for Name: document_payments; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.document_payments VALUES ('96b55d49-affc-4713-92ca-a323683cea37', '094b7e39-8e93-4f2c-9f8e-ed28bb7b46fe', '1', 'Efectivo', 10000.00, '2025-10-24 02:18:39.371248', NULL, 0.000000, NULL, NULL, NULL, NULL);
INSERT INTO public.document_payments VALUES ('d4f5ea2f-5309-4409-8356-314b5afcfa33', '137a470f-e20b-466c-83ed-e4031968855a', '1', 'Efectivo', 980000.00, '2025-10-26 23:29:14.805466', 'PYG', 0.000000, NULL, NULL, NULL, NULL);
INSERT INTO public.document_payments VALUES ('b41e5547-853e-431f-85c7-862e274a1a9b', '7d752b71-52c6-410b-aa88-6066fa2578d7', '1', 'Efectivo', 980000.00, '2025-10-27 00:18:51.681608', 'PYG', 0.000000, NULL, NULL, NULL, NULL);
INSERT INTO public.document_payments VALUES ('dbbc5599-d8e3-4cb2-9839-39d56af108e3', '81b343c9-5374-4bab-b586-38f9af2c1683', '1', 'Efectivo', 980000.00, '2025-10-28 12:44:18.02082', 'PYG', 0.000000, NULL, NULL, NULL, NULL);
INSERT INTO public.document_payments VALUES ('f9f386a8-f0c2-4089-9646-45ff56adab36', '6ef4f090-89ff-4675-954c-1eb506552151', '1', 'Efectivo', 980000.00, '2025-10-30 14:15:01.855503', 'PYG', 0.000000, NULL, NULL, NULL, NULL);
INSERT INTO public.document_payments VALUES ('2ac21052-4b43-4626-92b4-6e208b89cff0', '7aebdd10-5b31-4bd2-be8d-a9037a49ec38', '1', 'Efectivo', 980000.00, '2025-10-30 16:37:23.193704', 'PYG', 0.000000, NULL, NULL, NULL, NULL);
INSERT INTO public.document_payments VALUES ('4e9778e7-c992-45f3-83cd-b446d52bad2c', '7434864f-eb6c-4550-af10-adac152ba50d', '1', 'Efectivo', 980000.00, '2025-10-30 22:03:03.563076', 'PYG', 0.000000, NULL, NULL, NULL, NULL);


--
-- TOC entry 3751 (class 0 OID 36238)
-- Dependencies: 252
-- Data for Name: document_signatures; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3746 (class 0 OID 36175)
-- Dependencies: 247
-- Data for Name: documents; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.documents VALUES ('6ef4f090-89ff-4675-954c-1eb506552151', 'COMPANY1', 'FE', '001-001', 460, 'RECEIVED', 'erp-rcpt-001-20251026-145400', 'track-1761844501833', '2025-10-30 14:15:01.833544', '2025-10-30 14:15:01.833544', NULL, NULL, NULL, NULL, NULL, '2025-10-26 14:30:00', 'PYG', 0.000000, 0.00, 0.00, 890909.00, 0.00, 89091.00, 980000.00, '01', 1, '01', 1, '987654321', 980000.00, 0.00, '001', '001', NULL, 'Venta mostrador sucursal 001', NULL, NULL, NULL, NULL);
INSERT INTO public.documents VALUES ('7aebdd10-5b31-4bd2-be8d-a9037a49ec38', 'COMPANY1', 'FE', '001-001', 460, 'FAILED', 'erp-rcpt-001-20251026-145401', 'track-1761853035998', '2025-10-30 16:37:18.220115', '2025-10-30 16:57:56.393883', NULL, '80000001300100100004602025102614309876543219', NULL, NULL, 'XML validation failed: Missing UBL Invoice namespace, Missing SIFEN namespace, Missing or incorrect dVerFor=150, Missing CDC element with id="A001"', '2025-10-26 14:30:00', 'PYG', 0.000000, 0.00, 0.00, 890909.00, 0.00, 89091.00, 980000.00, '01', 1, '01', 1, '987654321', 980000.00, 0.00, '001', '001', NULL, 'Venta mostrador sucursal 001', NULL, NULL, NULL, '0000460');
INSERT INTO public.documents VALUES ('094b7e39-8e93-4f2c-9f8e-ed28bb7b46fe', 'COMPANY1', 'FE', '001-001', 1, 'RECEIVED', 'test001', 'track-1761283119348', '2025-10-24 02:18:39.349772', '2025-10-24 02:18:39.349772', NULL, NULL, NULL, NULL, NULL, '2025-10-24 02:18:39.349772', 'PYG', 0.000000, 0.00, 0.00, 10000.00, 0.00, 910.00, 10000.00, NULL, NULL, NULL, NULL, NULL, 10000.00, 0.00, '001', '001', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.documents VALUES ('137a470f-e20b-466c-83ed-e4031968855a', 'COMPANY1', 'FE', '001-001', 456, 'RECEIVED', 'erp-rcpt-001-20251026-145300', 'track-1761532128901', '2025-10-26 23:29:06.295381', '2025-10-26 23:29:06.295381', NULL, NULL, NULL, NULL, NULL, '2025-10-26 14:30:00', 'PYG', 0.000000, 0.00, 0.00, 890909.00, 0.00, 89091.00, 980000.00, '01', 1, '01', 1, '987654321', 980000.00, 0.00, '001', '001', NULL, 'Venta mostrador sucursal 001', NULL, NULL, NULL, NULL);
INSERT INTO public.documents VALUES ('7d752b71-52c6-410b-aa88-6066fa2578d7', 'COMPANY1', 'FE', '001-001', 457, 'RECEIVED', 'erp-rcpt-001-20251026-145301', 'track-1761535126503', '2025-10-27 00:18:46.509235', '2025-10-27 00:18:46.509235', NULL, NULL, NULL, NULL, NULL, '2025-10-26 14:30:00', 'PYG', 0.000000, 0.00, 0.00, 890909.00, 0.00, 89091.00, 980000.00, '01', 1, '01', 1, '987654321', 980000.00, 0.00, '001', '001', NULL, 'Venta mostrador sucursal 001', NULL, NULL, NULL, NULL);
INSERT INTO public.documents VALUES ('7434864f-eb6c-4550-af10-adac152ba50d', 'COMPANY1', 'FE', '001-001', 461, 'FAILED', 'erp-rcpt-001-20251026-145402', 'track-1761872583527', '2025-10-30 22:03:03.533643', '2025-11-03 17:24:42.056521', NULL, '80000001300100100004612025102614309876543214', NULL, NULL, 'XML validation failed: Missing XML declaration, Missing UBL Invoice namespace, Missing SIFEN namespace, Missing or incorrect dVerFor=150, Missing CDC element with id="A001"', '2025-10-26 14:30:00', 'PYG', 0.000000, 0.00, 0.00, 890909.00, 0.00, 89091.00, 980000.00, '01', 1, '01', 1, '987654321', 980000.00, 0.00, '001', '001', '12345678', 'Venta mostrador sucursal 001', NULL, NULL, NULL, '0000461');
INSERT INTO public.documents VALUES ('81b343c9-5374-4bab-b586-38f9af2c1683', 'COMPANY1', 'FE', '001-001', 459, 'FAILED', 'erp-rcpt-001-20251026-145303', 'track-1761666248816', '2025-10-28 12:44:08.824185', '2025-10-29 00:03:21.912654', NULL, '80000001300100100004592025102614309876543211', NULL, NULL, 'XML validation failed: Missing UBL Invoice namespace, Missing SIFEN namespace, Missing or incorrect dVerFor=150, Missing CDC element with id="A001"', '2025-10-26 14:30:00', 'PYG', 0.000000, 0.00, 0.00, 890909.00, 0.00, 89091.00, 980000.00, '01', 1, '01', 1, '987654321', 980000.00, 0.00, '001', '001', NULL, 'Venta mostrador sucursal 001', NULL, NULL, NULL, NULL);


--
-- TOC entry 3745 (class 0 OID 36166)
-- Dependencies: 246
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.flyway_schema_history VALUES (1, '0', '<< Flyway Baseline >>', 'BASELINE', '<< Flyway Baseline >>', NULL, 'postgres', '2025-10-16 23:23:53.881501', 0, true);
INSERT INTO public.flyway_schema_history VALUES (2, '1', 'Create SIFEN Tables', 'SQL', 'V1__Create_SIFEN_Tables.sql', 1265099008, 'postgres', '2025-10-16 23:23:54.403423', 597, true);


--
-- TOC entry 3726 (class 0 OID 26770)
-- Dependencies: 227
-- Data for Name: invoice; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3727 (class 0 OID 26790)
-- Dependencies: 228
-- Data for Name: invoice_item; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3728 (class 0 OID 26797)
-- Dependencies: 229
-- Data for Name: invoice_payment; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3737 (class 0 OID 26860)
-- Dependencies: 238
-- Data for Name: metodo_pago; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.metodo_pago VALUES ('1', 'Efectivo');
INSERT INTO public.metodo_pago VALUES ('2', 'Cheque');
INSERT INTO public.metodo_pago VALUES ('3', 'Tarjeta de crédito');
INSERT INTO public.metodo_pago VALUES ('4', 'Tarjeta de débito');
INSERT INTO public.metodo_pago VALUES ('5', 'Transferencia');
INSERT INTO public.metodo_pago VALUES ('6', 'Giro');
INSERT INTO public.metodo_pago VALUES ('7', 'Billetera electrónica');
INSERT INTO public.metodo_pago VALUES ('8', 'Tarjeta empresarial');
INSERT INTO public.metodo_pago VALUES ('9', 'Vale');
INSERT INTO public.metodo_pago VALUES ('10', 'Retención');
INSERT INTO public.metodo_pago VALUES ('11', 'Pago por anticipo');
INSERT INTO public.metodo_pago VALUES ('12', 'Valor fiscal');
INSERT INTO public.metodo_pago VALUES ('13', 'Valor comercial');
INSERT INTO public.metodo_pago VALUES ('14', 'Compensación');
INSERT INTO public.metodo_pago VALUES ('15', 'Permuta');
INSERT INTO public.metodo_pago VALUES ('16', 'Pago bancario');
INSERT INTO public.metodo_pago VALUES ('17', 'Pago Móvil');
INSERT INTO public.metodo_pago VALUES ('18', 'Donación');
INSERT INTO public.metodo_pago VALUES ('19', 'Promoción');
INSERT INTO public.metodo_pago VALUES ('20', 'Consumo Interno');
INSERT INTO public.metodo_pago VALUES ('21', 'Pago Electrónico');
INSERT INTO public.metodo_pago VALUES ('99', 'Otro');


--
-- TOC entry 3732 (class 0 OID 26825)
-- Dependencies: 233
-- Data for Name: moneda; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.moneda VALUES ('USD', 'Dólar estadounidense', '$');
INSERT INTO public.moneda VALUES ('EUR', 'Euro', '€');
INSERT INTO public.moneda VALUES ('PYG', 'Guaraní paraguayo', '₲');
INSERT INTO public.moneda VALUES ('ARS', 'Peso argentino', '$');
INSERT INTO public.moneda VALUES ('BRL', 'Real brasileño', 'R$');


--
-- TOC entry 3738 (class 0 OID 26867)
-- Dependencies: 239
-- Data for Name: motivo_emision; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.motivo_emision VALUES ('1', 'Traslado por venta');
INSERT INTO public.motivo_emision VALUES ('2', 'Traslado por consignación');
INSERT INTO public.motivo_emision VALUES ('3', 'Exportación');
INSERT INTO public.motivo_emision VALUES ('4', 'Traslado por compra');
INSERT INTO public.motivo_emision VALUES ('5', 'Importación');
INSERT INTO public.motivo_emision VALUES ('6', 'Traslado por devolución');
INSERT INTO public.motivo_emision VALUES ('7', 'Traslado entre locales de la empresa');
INSERT INTO public.motivo_emision VALUES ('8', 'Traslado de bienes por transformación');
INSERT INTO public.motivo_emision VALUES ('9', 'Traslado de bienes por reparación');
INSERT INTO public.motivo_emision VALUES ('10', 'Traslado por emisor móvil');
INSERT INTO public.motivo_emision VALUES ('11', 'Exhibición o demostración');
INSERT INTO public.motivo_emision VALUES ('12', 'Participación en ferias');
INSERT INTO public.motivo_emision VALUES ('13', 'Traslado de encomienda');
INSERT INTO public.motivo_emision VALUES ('14', 'Decomiso');
INSERT INTO public.motivo_emision VALUES ('99', 'Otro (describir)');


--
-- TOC entry 3744 (class 0 OID 27951)
-- Dependencies: 245
-- Data for Name: obligation; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.obligation VALUES ('cmgfvw2460005j2cgm682xive', 'IVA', 'IVA General', true, '2a056255-15d3-4bcc-ad90-1b18f5037874', '2025-10-07 01:31:23.958', '2025-10-07 01:31:23.958');
INSERT INTO public.obligation VALUES ('cmgfvw24s0007j2cgdx74cemm', 'IRP', 'Impuesto a la Renta Personal', true, '2a056255-15d3-4bcc-ad90-1b18f5037874', '2025-10-07 01:31:23.98', '2025-10-07 01:31:23.98');
INSERT INTO public.obligation VALUES ('cmgfznmwk000fj2mkwjbygn0y', '113', 'IMPUESTO A LA RENTA IRACIS - RÉGIMENES ESPECIALES', true, '07ca9fc4-f4da-4c4a-a578-eb863770e8ea', '2025-10-07 03:16:49.459', '2025-10-07 03:16:49.459');
INSERT INTO public.obligation VALUES ('cmgg02d7t0003j2is141kap2l', '211', 'IMPUESTO AL VALOR AGREGADO - GRAVADAS Y EXONERADAS - EXPORTADORES', true, '47e4fee1-42b3-4fac-bc73-fd68730dff90', '2025-10-07 03:28:16.745', '2025-10-07 03:28:16.745');
INSERT INTO public.obligation VALUES ('cmgg2mqdb0003j2q8c3e9r0gp', '211', 'IMPUESTO AL VALOR AGREGADO - GRAVADAS Y EXONERADAS - EXPORTADORES', true, 'b1b62708-5e61-421e-a440-94fa8b83d558', '2025-10-07 04:40:06.142', '2025-10-07 04:40:06.142');
INSERT INTO public.obligation VALUES ('cmgg41b620003j2rcb1lpdxl6', '311', 'IMPUESTO SELECTIVO AL CONSUMO - GENERAL', true, '2af8e083-8eb6-41c2-abfa-d7d6ee4ab6e1', '2025-10-07 05:19:25.898', '2025-10-07 05:19:25.898');
INSERT INTO public.obligation VALUES ('cmgg4boga0003j2kkyyten7xq', '311', 'IMPUESTO SELECTIVO AL CONSUMO - GENERAL', true, '821a7281-cc67-4e0b-8503-189900d98c0d', '2025-10-07 05:27:29.674', '2025-10-07 05:27:29.674');
INSERT INTO public.obligation VALUES ('cmgg4wulr0003j2bko9cd6cqk', '211', 'IMPUESTO AL VALOR AGREGADO - GRAVADAS Y EXONERADAS - EXPORTADORES', true, 'a6f80e29-af97-4f37-802b-21157e10b46b', '2025-10-07 05:43:57.423', '2025-10-07 05:43:57.423');
INSERT INTO public.obligation VALUES ('cmgg5dco90003j2lcnfxgbo9z', '211', 'IMPUESTO AL VALOR AGREGADO - GRAVADAS Y EXONERADAS - EXPORTADORES', true, '7788ac61-e7cc-4227-95b7-5c8351370ffc', '2025-10-07 05:56:47.337', '2025-10-07 05:56:47.337');


--
-- TOC entry 3750 (class 0 OID 36226)
-- Dependencies: 251
-- Data for Name: outbox_events; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.outbox_events VALUES ('f776958e-ec4c-4076-baa6-7020f4946559', '6ef4f090-89ff-4675-954c-1eb506552151', 'Document', 'DOCUMENT_PROCESS', 'Factura de contado creada y encolada para procesamiento', NULL, '2025-10-30 14:15:01.87251', NULL, 'PENDING', 0, NULL, 'erp-rcpt-001-20251026-145400', 'COMPANY1', 'track-1761844501833', NULL);
INSERT INTO public.outbox_events VALUES ('b554a191-2e20-467a-bb06-19fd16aa67d0', '7aebdd10-5b31-4bd2-be8d-a9037a49ec38', 'Document', 'DOCUMENT_PROCESS', 'Factura de contado creada y encolada para procesamiento', NULL, '2025-10-30 16:37:28.607785', NULL, 'PENDING', 0, NULL, 'erp-rcpt-001-20251026-145401', 'COMPANY1', 'track-1761853035998', NULL);
INSERT INTO public.outbox_events VALUES ('26fb19d1-ced6-44f7-a8ac-43fea876201c', '094b7e39-8e93-4f2c-9f8e-ed28bb7b46fe', 'Document', 'DOCUMENT_PROCESS', 'Factura de contado creada y encolada para procesamiento', NULL, '2025-10-24 02:19:16.287558', NULL, 'PROCESSED', 0, NULL, '094b7e39-8e93-4f2c-9f8e-ed28bb7b46fe', 'test-company-001', 'track-1761283119348', NULL);
INSERT INTO public.outbox_events VALUES ('8b8fd8bd-bdb3-4ee7-a871-175c6f6b0454', '137a470f-e20b-466c-83ed-e4031968855a', 'Document', 'DOCUMENT_PROCESS', 'Factura de contado creada y encolada para procesamiento', NULL, '2025-10-26 23:29:14.821226', NULL, 'PROCESSED', 0, NULL, '137a470f-e20b-466c-83ed-e4031968855a', 'test-company-001', 'track-1761532128901', NULL);
INSERT INTO public.outbox_events VALUES ('49f13dc9-e820-4a63-9f62-55d5dcb90f57', '7d752b71-52c6-410b-aa88-6066fa2578d7', 'Document', 'DOCUMENT_PROCESS', 'Factura de contado creada y encolada para procesamiento', NULL, '2025-10-27 00:18:51.69577', NULL, 'PROCESSED', 0, NULL, 'erp-rcpt-001-20251026-145301', 'test-company-001', 'track-1761535126503', NULL);
INSERT INTO public.outbox_events VALUES ('db08bf0d-30f3-42bb-a445-df6a4d5494e7', '81b343c9-5374-4bab-b586-38f9af2c1683', 'Document', 'DOCUMENT_PROCESS', 'Factura de contado creada y encolada para procesamiento', NULL, '2025-10-28 12:44:20.985325', NULL, 'PENDING', 0, 'java.net.ConnectException: Connection refused: no further information', 'erp-rcpt-001-20251026-145303', 'test-company-001', 'track-1761666248816', NULL);
INSERT INTO public.outbox_events VALUES ('53dfd5f4-30d2-4c7c-9684-ed7d29b825d1', '7434864f-eb6c-4550-af10-adac152ba50d', 'Document', 'DOCUMENT_PROCESS', 'Factura de contado creada y encolada para procesamiento', NULL, '2025-10-30 22:03:03.579381', NULL, 'PENDING', 0, NULL, 'erp-rcpt-001-20251026-145402', 'COMPANY1', 'track-1761872583527', NULL);


--
-- TOC entry 3733 (class 0 OID 26832)
-- Dependencies: 234
-- Data for Name: pais_receptor; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.pais_receptor VALUES ('USA', 'Estados Unidos');
INSERT INTO public.pais_receptor VALUES ('EUR', 'Unión Europea');
INSERT INTO public.pais_receptor VALUES ('PRY', 'Paraguay');
INSERT INTO public.pais_receptor VALUES ('ARG', 'Argentina');
INSERT INTO public.pais_receptor VALUES ('BRA', 'Brasil');


--
-- TOC entry 3716 (class 0 OID 26687)
-- Dependencies: 217
-- Data for Name: pos; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.pos VALUES ('2705bff0-a073-41df-a168-086d4226143a', 'ed13dfa8-fa1a-43f6-93c8-efdb3e4a5c19', '001', 'Punto de Venta Principal', true);
INSERT INTO public.pos VALUES ('POS1', 'BRANCH1', '001', 'POS principal', true);


--
-- TOC entry 3723 (class 0 OID 26740)
-- Dependencies: 224
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3739 (class 0 OID 26874)
-- Dependencies: 240
-- Data for Name: responsable_emision; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.responsable_emision VALUES ('1', 'Emisor de la factura');
INSERT INTO public.responsable_emision VALUES ('2', 'Poseedor de la factura y bienes');
INSERT INTO public.responsable_emision VALUES ('3', 'Empresa transportista');
INSERT INTO public.responsable_emision VALUES ('4', 'Despachante de Aduanas');
INSERT INTO public.responsable_emision VALUES ('5', 'Agente de transporte o intermediario');


--
-- TOC entry 3722 (class 0 OID 26732)
-- Dependencies: 223
-- Data for Name: tax; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tax VALUES (1, 'EXENTA', 0, 'Exenta');
INSERT INTO public.tax VALUES (2, 'IVA5', 5, 'IVA 5%');
INSERT INTO public.tax VALUES (3, 'IVA10', 10, 'IVA 10%');


--
-- TOC entry 3720 (class 0 OID 26723)
-- Dependencies: 221
-- Data for Name: timbrado; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.timbrado VALUES ('96f36311-9c5c-4568-9e00-4320386b5d84', '2a056255-15d3-4bcc-ad90-1b18f5037874', '12345678', '2024-01-01 00:00:00', '2024-12-31 00:00:00', 'ed13dfa8-fa1a-43f6-93c8-efdb3e4a5c19', '2705bff0-a073-41df-a168-086d4226143a', true);
INSERT INTO public.timbrado VALUES ('TIMB1', 'COMPANY1', '12345678', '2024-01-01 00:00:00', '2025-12-31 00:00:00', 'BRANCH1', 'POS1', true);


--
-- TOC entry 3729 (class 0 OID 26804)
-- Dependencies: 230
-- Data for Name: tipo_documento_electronico; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tipo_documento_electronico VALUES ('1', 'Factura electrónica');
INSERT INTO public.tipo_documento_electronico VALUES ('2', 'Factura electrónica de exportación (Futuro)');
INSERT INTO public.tipo_documento_electronico VALUES ('3', 'Factura electrónica de importación (Futuro)');
INSERT INTO public.tipo_documento_electronico VALUES ('4', 'Autofactura electrónica');
INSERT INTO public.tipo_documento_electronico VALUES ('5', 'Nota de crédito electrónica');
INSERT INTO public.tipo_documento_electronico VALUES ('6', 'Nota de débito electrónica');
INSERT INTO public.tipo_documento_electronico VALUES ('7', 'Nota de remisión electrónica');
INSERT INTO public.tipo_documento_electronico VALUES ('8', 'Comprobante de retención electrónico (Futuro)');


--
-- TOC entry 3734 (class 0 OID 26839)
-- Dependencies: 235
-- Data for Name: tipo_documento_identidad; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tipo_documento_identidad VALUES ('1', 'Cédula paraguaya');
INSERT INTO public.tipo_documento_identidad VALUES ('2', 'Pasaporte');
INSERT INTO public.tipo_documento_identidad VALUES ('3', 'Cédula extranjera');
INSERT INTO public.tipo_documento_identidad VALUES ('4', 'Carnet de residencia');
INSERT INTO public.tipo_documento_identidad VALUES ('5', 'Innominado');
INSERT INTO public.tipo_documento_identidad VALUES ('6', 'Tarjeta Diplomática de exoneración fiscal');
INSERT INTO public.tipo_documento_identidad VALUES ('9', 'Otro');


--
-- TOC entry 3736 (class 0 OID 26853)
-- Dependencies: 237
-- Data for Name: tipo_iva; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tipo_iva VALUES ('1', 'Gravado IVA');
INSERT INTO public.tipo_iva VALUES ('2', 'Exonerado (Art.83 - 125)');
INSERT INTO public.tipo_iva VALUES ('3', 'Exento');
INSERT INTO public.tipo_iva VALUES ('4', 'Gravado parcial');


--
-- TOC entry 3731 (class 0 OID 26818)
-- Dependencies: 232
-- Data for Name: tipo_operacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tipo_operacion VALUES ('1', 'B2B (Business to Business)');
INSERT INTO public.tipo_operacion VALUES ('2', 'B2C (Business to Consumer)');
INSERT INTO public.tipo_operacion VALUES ('3', 'B2G (Business to Government)');
INSERT INTO public.tipo_operacion VALUES ('4', 'B2F (Business to Foreign) - Solo para servicios a empresas o personas físicas del exterior');


--
-- TOC entry 3730 (class 0 OID 26811)
-- Dependencies: 231
-- Data for Name: tipo_transaccion; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tipo_transaccion VALUES ('1', 'Venta de mercadería');
INSERT INTO public.tipo_transaccion VALUES ('2', 'Prestación de servicios');
INSERT INTO public.tipo_transaccion VALUES ('3', 'Mixto (Venta de mercadería y servicios)');
INSERT INTO public.tipo_transaccion VALUES ('4', 'Venta de activo fijo');
INSERT INTO public.tipo_transaccion VALUES ('5', 'Venta de divisas');
INSERT INTO public.tipo_transaccion VALUES ('6', 'Compra de divisas');
INSERT INTO public.tipo_transaccion VALUES ('7', 'Promoción o entrega de muestras');
INSERT INTO public.tipo_transaccion VALUES ('8', 'Donación');
INSERT INTO public.tipo_transaccion VALUES ('9', 'Anticipo');
INSERT INTO public.tipo_transaccion VALUES ('10', 'Compra de productos');
INSERT INTO public.tipo_transaccion VALUES ('11', 'Compra de servicios');
INSERT INTO public.tipo_transaccion VALUES ('12', 'Venta de crédito fiscal');
INSERT INTO public.tipo_transaccion VALUES ('13', 'Muestras médicas (Art. 3 RG 24/2014)');


--
-- TOC entry 3735 (class 0 OID 26846)
-- Dependencies: 236
-- Data for Name: unidad_medida; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.unidad_medida VALUES ('77', 'Unidad (UNI)');
INSERT INTO public.unidad_medida VALUES ('83', 'Kilogramos (kg)');
INSERT INTO public.unidad_medida VALUES ('87', 'Metros (m)');
INSERT INTO public.unidad_medida VALUES ('2366', 'Costo por Mil (CPM)');
INSERT INTO public.unidad_medida VALUES ('2329', 'Unidad Internacional (UI)');
INSERT INTO public.unidad_medida VALUES ('110', 'Metros cúbicos (M3)');
INSERT INTO public.unidad_medida VALUES ('86', 'Gramos (g)');
INSERT INTO public.unidad_medida VALUES ('89', 'Litros (LT)');
INSERT INTO public.unidad_medida VALUES ('90', 'Miligramos (MG)');
INSERT INTO public.unidad_medida VALUES ('91', 'Centímetros (CM)');
INSERT INTO public.unidad_medida VALUES ('92', 'Centímetros cuadrados (CM2)');
INSERT INTO public.unidad_medida VALUES ('93', 'Centímetros cúbicos (CM3)');
INSERT INTO public.unidad_medida VALUES ('94', 'Pulgadas (PUL)');
INSERT INTO public.unidad_medida VALUES ('96', 'Milímetros cuadrados (MM2)');
INSERT INTO public.unidad_medida VALUES ('79', 'Kilogramos s/ metro cuadrado (kg/m²)');
INSERT INTO public.unidad_medida VALUES ('97', 'Año (AA)');
INSERT INTO public.unidad_medida VALUES ('98', 'Mes (ME)');
INSERT INTO public.unidad_medida VALUES ('99', 'Tonelada (TN)');
INSERT INTO public.unidad_medida VALUES ('100', 'Hora (Hs)');
INSERT INTO public.unidad_medida VALUES ('101', 'Minuto (Mi)');
INSERT INTO public.unidad_medida VALUES ('104', 'Determinación (DET)');
INSERT INTO public.unidad_medida VALUES ('103', 'Yardas (Ya)');
INSERT INTO public.unidad_medida VALUES ('625', 'Kilómetros (Km)');
INSERT INTO public.unidad_medida VALUES ('660', 'Metro lineal (ml)');


--
-- TOC entry 3717 (class 0 OID 26695)
-- Dependencies: 218
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public."user" VALUES ('1faec497-75ee-48db-b03e-f120a1fb7aea', '2a056255-15d3-4bcc-ad90-1b18f5037874', 'admin@demo.com', 'Administrador Demo', '$2b$10$GP/g1mocMQCmOSSGgbnlTugRLcxNdB1S0yRQ3NT.VZPhxWnyHl1XG', 'admin', true, '2025-10-07 01:31:23.9', '$2b$10$pXrQf/TNHHgAYAAj6u0FrO5Nb8hJFjlHKvrI4NZC53BJVs5lCIuQy');


--
-- TOC entry 3763 (class 0 OID 0)
-- Dependencies: 242
-- Name: audit_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audit_log_id_seq', 1, false);


--
-- TOC entry 3764 (class 0 OID 0)
-- Dependencies: 222
-- Name: tax_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tax_id_seq', 3, true);


--
-- TOC entry 3404 (class 2606 OID 26669)
-- Name: _prisma_migrations _prisma_migrations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public._prisma_migrations
    ADD CONSTRAINT _prisma_migrations_pkey PRIMARY KEY (id);


--
-- TOC entry 3471 (class 2606 OID 27950)
-- Name: activity activity_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.activity
    ADD CONSTRAINT activity_pkey PRIMARY KEY (id);


--
-- TOC entry 3468 (class 2606 OID 26897)
-- Name: audit_log audit_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit_log
    ADD CONSTRAINT audit_log_pkey PRIMARY KEY (id);


--
-- TOC entry 3410 (class 2606 OID 26686)
-- Name: branch branch_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.branch
    ADD CONSTRAINT branch_pkey PRIMARY KEY (id);


--
-- TOC entry 3418 (class 2606 OID 26713)
-- Name: certificate certificate_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificate
    ADD CONSTRAINT certificate_pkey PRIMARY KEY (id);


--
-- TOC entry 3508 (class 2606 OID 36261)
-- Name: company_certificates company_certificates_company_id_certificate_alias_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_certificates
    ADD CONSTRAINT company_certificates_company_id_certificate_alias_key UNIQUE (company_id, certificate_alias);


--
-- TOC entry 3510 (class 2606 OID 36259)
-- Name: company_certificates company_certificates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_certificates
    ADD CONSTRAINT company_certificates_pkey PRIMARY KEY (id);


--
-- TOC entry 3406 (class 2606 OID 26679)
-- Name: company company_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company
    ADD CONSTRAINT company_pkey PRIMARY KEY (id);


--
-- TOC entry 3512 (class 2606 OID 36270)
-- Name: company_timbrados company_timbrados_company_id_timbrado_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_timbrados
    ADD CONSTRAINT company_timbrados_company_id_timbrado_number_key UNIQUE (company_id, timbrado_number);


--
-- TOC entry 3514 (class 2606 OID 36268)
-- Name: company_timbrados company_timbrados_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company_timbrados
    ADD CONSTRAINT company_timbrados_pkey PRIMARY KEY (id);


--
-- TOC entry 3420 (class 2606 OID 26722)
-- Name: csc csc_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.csc
    ADD CONSTRAINT csc_pkey PRIMARY KEY (id);


--
-- TOC entry 3432 (class 2606 OID 26759)
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);


--
-- TOC entry 3466 (class 2606 OID 26887)
-- Name: denominacion_tarjeta denominacion_tarjeta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.denominacion_tarjeta
    ADD CONSTRAINT denominacion_tarjeta_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3435 (class 2606 OID 26769)
-- Name: doc_seq doc_seq_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doc_seq
    ADD CONSTRAINT doc_seq_pkey PRIMARY KEY (id);


--
-- TOC entry 3516 (class 2606 OID 36286)
-- Name: document_customers document_customers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_customers
    ADD CONSTRAINT document_customers_pkey PRIMARY KEY (id);


--
-- TOC entry 3497 (class 2606 OID 36220)
-- Name: document_events document_events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_events
    ADD CONSTRAINT document_events_pkey PRIMARY KEY (id);


--
-- TOC entry 3490 (class 2606 OID 36196)
-- Name: document_items document_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_items
    ADD CONSTRAINT document_items_pkey PRIMARY KEY (id);


--
-- TOC entry 3493 (class 2606 OID 36207)
-- Name: document_payments document_payments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_payments
    ADD CONSTRAINT document_payments_pkey PRIMARY KEY (id);


--
-- TOC entry 3505 (class 2606 OID 36245)
-- Name: document_signatures document_signatures_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_signatures
    ADD CONSTRAINT document_signatures_pkey PRIMARY KEY (id);


--
-- TOC entry 3479 (class 2606 OID 36186)
-- Name: documents documents_company_id_idempotency_key_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documents
    ADD CONSTRAINT documents_company_id_idempotency_key_key UNIQUE (company_id, idempotency_key);


--
-- TOC entry 3481 (class 2606 OID 36354)
-- Name: documents documents_company_id_series_number_timb_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documents
    ADD CONSTRAINT documents_company_id_series_number_timb_key UNIQUE (company_id, series, number, timbrado_number);


--
-- TOC entry 3483 (class 2606 OID 36184)
-- Name: documents documents_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documents
    ADD CONSTRAINT documents_pkey PRIMARY KEY (id);


--
-- TOC entry 3476 (class 2606 OID 36173)
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 3440 (class 2606 OID 26796)
-- Name: invoice_item invoice_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_item
    ADD CONSTRAINT invoice_item_pkey PRIMARY KEY (id);


--
-- TOC entry 3442 (class 2606 OID 26803)
-- Name: invoice_payment invoice_payment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_payment
    ADD CONSTRAINT invoice_payment_pkey PRIMARY KEY (id);


--
-- TOC entry 3438 (class 2606 OID 26789)
-- Name: invoice invoice_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_pkey PRIMARY KEY (id);


--
-- TOC entry 3460 (class 2606 OID 26866)
-- Name: metodo_pago metodo_pago_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.metodo_pago
    ADD CONSTRAINT metodo_pago_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3450 (class 2606 OID 26831)
-- Name: moneda moneda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.moneda
    ADD CONSTRAINT moneda_pkey PRIMARY KEY ("codigoISO");


--
-- TOC entry 3462 (class 2606 OID 26873)
-- Name: motivo_emision motivo_emision_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.motivo_emision
    ADD CONSTRAINT motivo_emision_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3474 (class 2606 OID 27960)
-- Name: obligation obligation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obligation
    ADD CONSTRAINT obligation_pkey PRIMARY KEY (id);


--
-- TOC entry 3501 (class 2606 OID 36237)
-- Name: outbox_events outbox_events_company_id_idempotency_key_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.outbox_events
    ADD CONSTRAINT outbox_events_company_id_idempotency_key_key UNIQUE (company_id, idempotency_key);


--
-- TOC entry 3503 (class 2606 OID 36235)
-- Name: outbox_events outbox_events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.outbox_events
    ADD CONSTRAINT outbox_events_pkey PRIMARY KEY (id);


--
-- TOC entry 3452 (class 2606 OID 26838)
-- Name: pais_receptor pais_receptor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pais_receptor
    ADD CONSTRAINT pais_receptor_pkey PRIMARY KEY ("codigoISO3");


--
-- TOC entry 3413 (class 2606 OID 26694)
-- Name: pos pos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pos
    ADD CONSTRAINT pos_pkey PRIMARY KEY (id);


--
-- TOC entry 3429 (class 2606 OID 26749)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- TOC entry 3464 (class 2606 OID 26880)
-- Name: responsable_emision responsable_emision_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.responsable_emision
    ADD CONSTRAINT responsable_emision_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3426 (class 2606 OID 26739)
-- Name: tax tax_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tax
    ADD CONSTRAINT tax_pkey PRIMARY KEY (id);


--
-- TOC entry 3423 (class 2606 OID 26730)
-- Name: timbrado timbrado_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timbrado
    ADD CONSTRAINT timbrado_pkey PRIMARY KEY (id);


--
-- TOC entry 3444 (class 2606 OID 26810)
-- Name: tipo_documento_electronico tipo_documento_electronico_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_documento_electronico
    ADD CONSTRAINT tipo_documento_electronico_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3454 (class 2606 OID 26845)
-- Name: tipo_documento_identidad tipo_documento_identidad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_documento_identidad
    ADD CONSTRAINT tipo_documento_identidad_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3458 (class 2606 OID 26859)
-- Name: tipo_iva tipo_iva_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_iva
    ADD CONSTRAINT tipo_iva_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3448 (class 2606 OID 26824)
-- Name: tipo_operacion tipo_operacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_operacion
    ADD CONSTRAINT tipo_operacion_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3446 (class 2606 OID 26817)
-- Name: tipo_transaccion tipo_transaccion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_transaccion
    ADD CONSTRAINT tipo_transaccion_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3456 (class 2606 OID 26852)
-- Name: unidad_medida unidad_medida_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.unidad_medida
    ADD CONSTRAINT unidad_medida_pkey PRIMARY KEY (codigo);


--
-- TOC entry 3416 (class 2606 OID 26704)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 3469 (class 1259 OID 27961)
-- Name: activity_company_id_code_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX activity_company_id_code_key ON public.activity USING btree (company_id, code);


--
-- TOC entry 3408 (class 1259 OID 26899)
-- Name: branch_company_id_code_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX branch_company_id_code_key ON public.branch USING btree (company_id, code);


--
-- TOC entry 3407 (class 1259 OID 26898)
-- Name: company_ruc_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX company_ruc_key ON public.company USING btree (ruc);


--
-- TOC entry 3430 (class 1259 OID 26905)
-- Name: customer_company_id_doc_type_doc_number_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX customer_company_id_doc_type_doc_number_key ON public.customer USING btree (company_id, doc_type, doc_number);


--
-- TOC entry 3433 (class 1259 OID 26906)
-- Name: doc_seq_company_id_timbrado_id_branch_id_pos_id_doc_type_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX doc_seq_company_id_timbrado_id_branch_id_pos_id_doc_type_key ON public.doc_seq USING btree (company_id, timbrado_id, branch_id, pos_id, doc_type);


--
-- TOC entry 3477 (class 1259 OID 36174)
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- TOC entry 3494 (class 1259 OID 36337)
-- Name: idx_doc_payments_doc; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_doc_payments_doc ON public.document_payments USING btree (document_id);


--
-- TOC entry 3495 (class 1259 OID 36338)
-- Name: idx_doc_payments_method; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_doc_payments_method ON public.document_payments USING btree (payment_method_code);


--
-- TOC entry 3517 (class 1259 OID 36344)
-- Name: idx_document_customers_doc; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_document_customers_doc ON public.document_customers USING btree (document_id);


--
-- TOC entry 3518 (class 1259 OID 36345)
-- Name: idx_document_customers_docid; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_document_customers_docid ON public.document_customers USING btree (doc_type, doc_number);


--
-- TOC entry 3519 (class 1259 OID 36293)
-- Name: idx_document_customers_nombre; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_document_customers_nombre ON public.document_customers USING btree (nombre);


--
-- TOC entry 3520 (class 1259 OID 36292)
-- Name: idx_document_customers_ruc; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_document_customers_ruc ON public.document_customers USING btree (ruc);


--
-- TOC entry 3498 (class 1259 OID 36274)
-- Name: idx_document_events_document; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_document_events_document ON public.document_events USING btree (document_id, created_at);


--
-- TOC entry 3491 (class 1259 OID 36325)
-- Name: idx_document_items_document_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_document_items_document_id ON public.document_items USING btree (document_id);


--
-- TOC entry 3506 (class 1259 OID 36275)
-- Name: idx_document_signatures_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_document_signatures_company ON public.document_signatures USING btree (company_id, created_at);


--
-- TOC entry 3484 (class 1259 OID 36314)
-- Name: idx_documents_cdc; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_documents_cdc ON public.documents USING btree (cdc);


--
-- TOC entry 3485 (class 1259 OID 36271)
-- Name: idx_documents_company_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_documents_company_status ON public.documents USING btree (company_id, status);


--
-- TOC entry 3486 (class 1259 OID 36347)
-- Name: idx_documents_condicion_pago; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_documents_condicion_pago ON public.documents USING btree (condicion_pago);


--
-- TOC entry 3487 (class 1259 OID 36272)
-- Name: idx_documents_idempotency; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_documents_idempotency ON public.documents USING btree (company_id, idempotency_key);


--
-- TOC entry 3488 (class 1259 OID 36346)
-- Name: idx_documents_tipo_documento; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_documents_tipo_documento ON public.documents USING btree (tipo_documento);


--
-- TOC entry 3499 (class 1259 OID 36273)
-- Name: idx_outbox_events_status; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_outbox_events_status ON public.outbox_events USING btree (status, created_at);


--
-- TOC entry 3436 (class 1259 OID 26907)
-- Name: invoice_company_id_series_number_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX invoice_company_id_series_number_key ON public.invoice USING btree (company_id, series, number);


--
-- TOC entry 3472 (class 1259 OID 27962)
-- Name: obligation_company_id_code_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX obligation_company_id_code_key ON public.obligation USING btree (company_id, code);


--
-- TOC entry 3411 (class 1259 OID 26900)
-- Name: pos_branch_id_code_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX pos_branch_id_code_key ON public.pos USING btree (branch_id, code);


--
-- TOC entry 3427 (class 1259 OID 26904)
-- Name: product_company_id_sku_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX product_company_id_sku_key ON public.product USING btree (company_id, sku);


--
-- TOC entry 3424 (class 1259 OID 26903)
-- Name: tax_code_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX tax_code_key ON public.tax USING btree (code);


--
-- TOC entry 3421 (class 1259 OID 26902)
-- Name: timbrado_company_id_number_branch_id_pos_id_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX timbrado_company_id_number_branch_id_pos_id_key ON public.timbrado USING btree (company_id, number, branch_id, pos_id);


--
-- TOC entry 3414 (class 1259 OID 26901)
-- Name: user_email_key; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX user_email_key ON public."user" USING btree (email);


--
-- TOC entry 3558 (class 2606 OID 27963)
-- Name: activity activity_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.activity
    ADD CONSTRAINT activity_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3521 (class 2606 OID 26908)
-- Name: branch branch_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.branch
    ADD CONSTRAINT branch_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3524 (class 2606 OID 26923)
-- Name: certificate certificate_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificate
    ADD CONSTRAINT certificate_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3525 (class 2606 OID 26928)
-- Name: csc csc_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.csc
    ADD CONSTRAINT csc_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3533 (class 2606 OID 26968)
-- Name: customer customer_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3534 (class 2606 OID 26973)
-- Name: customer customer_pais_receptor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pais_receptor_id_fkey FOREIGN KEY (pais_receptor_id) REFERENCES public.pais_receptor("codigoISO3") ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3535 (class 2606 OID 26978)
-- Name: customer customer_tipo_documento_identidad_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_tipo_documento_identidad_id_fkey FOREIGN KEY (tipo_documento_identidad_id) REFERENCES public.tipo_documento_identidad(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3536 (class 2606 OID 26993)
-- Name: doc_seq doc_seq_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doc_seq
    ADD CONSTRAINT doc_seq_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branch(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3537 (class 2606 OID 26983)
-- Name: doc_seq doc_seq_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doc_seq
    ADD CONSTRAINT doc_seq_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3538 (class 2606 OID 26998)
-- Name: doc_seq doc_seq_pos_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doc_seq
    ADD CONSTRAINT doc_seq_pos_id_fkey FOREIGN KEY (pos_id) REFERENCES public.pos(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3539 (class 2606 OID 26988)
-- Name: doc_seq doc_seq_timbrado_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doc_seq
    ADD CONSTRAINT doc_seq_timbrado_id_fkey FOREIGN KEY (timbrado_id) REFERENCES public.timbrado(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3569 (class 2606 OID 36287)
-- Name: document_customers document_customers_document_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_customers
    ADD CONSTRAINT document_customers_document_id_fkey FOREIGN KEY (document_id) REFERENCES public.documents(id) ON DELETE CASCADE;


--
-- TOC entry 3567 (class 2606 OID 36221)
-- Name: document_events document_events_document_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_events
    ADD CONSTRAINT document_events_document_id_fkey FOREIGN KEY (document_id) REFERENCES public.documents(id) ON DELETE CASCADE;


--
-- TOC entry 3561 (class 2606 OID 36197)
-- Name: document_items document_items_document_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_items
    ADD CONSTRAINT document_items_document_id_fkey FOREIGN KEY (document_id) REFERENCES public.documents(id) ON DELETE CASCADE;


--
-- TOC entry 3564 (class 2606 OID 36208)
-- Name: document_payments document_payments_document_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_payments
    ADD CONSTRAINT document_payments_document_id_fkey FOREIGN KEY (document_id) REFERENCES public.documents(id) ON DELETE CASCADE;


--
-- TOC entry 3568 (class 2606 OID 36246)
-- Name: document_signatures document_signatures_document_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_signatures
    ADD CONSTRAINT document_signatures_document_id_fkey FOREIGN KEY (document_id) REFERENCES public.documents(id) ON DELETE SET NULL;


--
-- TOC entry 3570 (class 2606 OID 36339)
-- Name: document_customers fk_doc_customers_tipo_doc_ident; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_customers
    ADD CONSTRAINT fk_doc_customers_tipo_doc_ident FOREIGN KEY (tipo_documento_identidad_id) REFERENCES public.tipo_documento_identidad(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3562 (class 2606 OID 36315)
-- Name: document_items fk_doc_items_tipo_iva; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_items
    ADD CONSTRAINT fk_doc_items_tipo_iva FOREIGN KEY (tipo_iva_code) REFERENCES public.tipo_iva(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3563 (class 2606 OID 36320)
-- Name: document_items fk_doc_items_unidad_medida; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_items
    ADD CONSTRAINT fk_doc_items_unidad_medida FOREIGN KEY (unit_measure_code) REFERENCES public.unidad_medida(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3565 (class 2606 OID 36332)
-- Name: document_payments fk_doc_payments_card_brand; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_payments
    ADD CONSTRAINT fk_doc_payments_card_brand FOREIGN KEY (card_brand_code) REFERENCES public.denominacion_tarjeta(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3566 (class 2606 OID 36327)
-- Name: document_payments fk_doc_payments_metodo_pago; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.document_payments
    ADD CONSTRAINT fk_doc_payments_metodo_pago FOREIGN KEY (payment_method_code) REFERENCES public.metodo_pago(codigo) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3560 (class 2606 OID 36348)
-- Name: documents fk_documents_company; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.documents
    ADD CONSTRAINT fk_documents_company FOREIGN KEY (company_id) REFERENCES public.company(id);


--
-- TOC entry 3540 (class 2606 OID 27018)
-- Name: invoice invoice_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branch(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3541 (class 2606 OID 27003)
-- Name: invoice invoice_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3542 (class 2606 OID 27008)
-- Name: invoice invoice_customer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES public.customer(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3550 (class 2606 OID 27053)
-- Name: invoice_item invoice_item_invoice_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_item
    ADD CONSTRAINT invoice_item_invoice_id_fkey FOREIGN KEY (invoice_id) REFERENCES public.invoice(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3551 (class 2606 OID 27058)
-- Name: invoice_item invoice_item_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_item
    ADD CONSTRAINT invoice_item_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.product(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3552 (class 2606 OID 27063)
-- Name: invoice_item invoice_item_tax_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_item
    ADD CONSTRAINT invoice_item_tax_id_fkey FOREIGN KEY (tax_id) REFERENCES public.tax(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3553 (class 2606 OID 27073)
-- Name: invoice_item invoice_item_tipo_iva_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_item
    ADD CONSTRAINT invoice_item_tipo_iva_id_fkey FOREIGN KEY (tipo_iva_id) REFERENCES public.tipo_iva(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3554 (class 2606 OID 27068)
-- Name: invoice_item invoice_item_unidad_medida_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_item
    ADD CONSTRAINT invoice_item_unidad_medida_id_fkey FOREIGN KEY (unidad_medida_id) REFERENCES public.unidad_medida(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3543 (class 2606 OID 27043)
-- Name: invoice invoice_moneda_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_moneda_id_fkey FOREIGN KEY (moneda_id) REFERENCES public.moneda("codigoISO") ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3544 (class 2606 OID 27048)
-- Name: invoice invoice_motivo_emision_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_motivo_emision_id_fkey FOREIGN KEY (motivo_emision_id) REFERENCES public.motivo_emision(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3555 (class 2606 OID 27088)
-- Name: invoice_payment invoice_payment_denominacion_tarjeta_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_payment
    ADD CONSTRAINT invoice_payment_denominacion_tarjeta_id_fkey FOREIGN KEY (denominacion_tarjeta_id) REFERENCES public.denominacion_tarjeta(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3556 (class 2606 OID 27078)
-- Name: invoice_payment invoice_payment_invoice_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_payment
    ADD CONSTRAINT invoice_payment_invoice_id_fkey FOREIGN KEY (invoice_id) REFERENCES public.invoice(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3557 (class 2606 OID 27083)
-- Name: invoice_payment invoice_payment_metodo_pago_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_payment
    ADD CONSTRAINT invoice_payment_metodo_pago_id_fkey FOREIGN KEY (metodo_pago_id) REFERENCES public.metodo_pago(codigo) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3545 (class 2606 OID 27023)
-- Name: invoice invoice_pos_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_pos_id_fkey FOREIGN KEY (pos_id) REFERENCES public.pos(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3546 (class 2606 OID 27013)
-- Name: invoice invoice_timbrado_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_timbrado_id_fkey FOREIGN KEY (timbrado_id) REFERENCES public.timbrado(id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 3547 (class 2606 OID 27028)
-- Name: invoice invoice_tipo_documento_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_tipo_documento_id_fkey FOREIGN KEY (tipo_documento_id) REFERENCES public.tipo_documento_electronico(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3548 (class 2606 OID 27038)
-- Name: invoice invoice_tipo_operacion_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_tipo_operacion_id_fkey FOREIGN KEY (tipo_operacion_id) REFERENCES public.tipo_operacion(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3549 (class 2606 OID 27033)
-- Name: invoice invoice_tipo_transaccion_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_tipo_transaccion_id_fkey FOREIGN KEY (tipo_transaccion_id) REFERENCES public.tipo_transaccion(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3559 (class 2606 OID 27968)
-- Name: obligation obligation_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.obligation
    ADD CONSTRAINT obligation_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3522 (class 2606 OID 26913)
-- Name: pos pos_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pos
    ADD CONSTRAINT pos_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branch(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3529 (class 2606 OID 26948)
-- Name: product product_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3530 (class 2606 OID 26953)
-- Name: product product_tax_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_tax_id_fkey FOREIGN KEY (tax_id) REFERENCES public.tax(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3531 (class 2606 OID 26963)
-- Name: product product_tipo_iva_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_tipo_iva_id_fkey FOREIGN KEY (tipo_iva_id) REFERENCES public.tipo_iva(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3532 (class 2606 OID 26958)
-- Name: product product_unidad_medida_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_unidad_medida_id_fkey FOREIGN KEY (unidad_medida_id) REFERENCES public.unidad_medida(codigo) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3526 (class 2606 OID 26938)
-- Name: timbrado timbrado_branch_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timbrado
    ADD CONSTRAINT timbrado_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branch(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3527 (class 2606 OID 26933)
-- Name: timbrado timbrado_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timbrado
    ADD CONSTRAINT timbrado_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3528 (class 2606 OID 26943)
-- Name: timbrado timbrado_pos_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.timbrado
    ADD CONSTRAINT timbrado_pos_id_fkey FOREIGN KEY (pos_id) REFERENCES public.pos(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 3523 (class 2606 OID 26918)
-- Name: user user_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_company_id_fkey FOREIGN KEY (company_id) REFERENCES public.company(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3760 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;


-- Completed on 2025-11-03 19:22:43

--
-- PostgreSQL database dump complete
--

