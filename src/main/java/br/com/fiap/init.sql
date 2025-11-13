-- Tabela usuários
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('candidate', 'company', 'admin')),
    created_at TEXT DEFAULT (datetime('now'))
);

-- Tabela candidatos
CREATE TABLE candidates (
    user_id INTEGER PRIMARY KEY,
    display_name TEXT, --Nome de exibição
    purpose TEXT, -- Objetivo profissional
    work_style TEXT, -- Estilo de trabalho
    interests TEXT, -- Interesses
    created_at TEXT DEFAULT (datetime('now')),
    FOREIGN KEY(user_id) REFERENCES users(id)
);

-- Tabela habilidades dos candidatos
CREATE TABLE candidate_skills (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    candidate_id INTEGER,
    skill_name TEXT, -- Nome da habilidade
    level INTEGER CHECK(level BETWEEN 1 AND 10),
    FOREIGN KEY(candidate_id) REFERENCES candidates(user_id)
);

-- Tabela experiências dos candidatos
CREATE TABLE candidate_experience (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    candidate_id INTEGER,
    company TEXT,
    role TEXT,
    description TEXT,
    start_date TEXT,
    end_date TEXT,
    FOREIGN KEY(candidate_id) REFERENCES candidates(user_id)
);

-- Tabela educação dos candidatos
CREATE TABLE candidate_education (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    candidate_id INTEGER,
    institution TEXT,
    course TEXT,
    level TEXT,
    start_date TEXT,
    end_date TEXT,
    FOREIGN KEY(candidate_id) REFERENCES candidates(user_id)
);

-- Tabela perguntas comportamentais
CREATE TABLE candidate_behavior_answers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    candidate_id INTEGER,
    question_id INTEGER,
    answer_value INTEGER,
    created_at TEXT DEFAULT (datetime('now')),
    FOREIGN KEY(candidate_id) REFERENCES candidates(user_id)
);

-- Tabela perfil comportamental gerado pela IA
CREATE TABLE candidate_behavior_profile (
    candidate_id INTEGER PRIMARY KEY,
    ai_profile TEXT, -- perfil criado pela ia
    last_updated TEXT DEFAULT (datetime('now')),
    FOREIGN KEY(candidate_id) REFERENCES candidates(user_id)
);

-- Tabela empresas
CREATE TABLE companies (
    user_id INTEGER PRIMARY KEY,
    name TEXT,
    description TEXT,
    industry TEXT,
    created_at TEXT DEFAULT (datetime('now')),
    FOREIGN KEY(user_id) REFERENCES users(id)
);

-- Tabela traços culturais das empresas
CREATE TABLE company_culture_traits (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    company_id INTEGER,
    trait TEXT, -- traço cultural (Ex: inovação, colaboração)
    value INTEGER CHECK(value BETWEEN 1 AND 10),
    FOREIGN KEY(company_id) REFERENCES companies(user_id)
);

-- Tabela vagas de emprego
CREATE TABLE jobs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    company_id INTEGER,
    title TEXT,
    description TEXT,
    salary REAL,
    work_model TEXT, -- remoto, híbrido, presencial
    created_at TEXT DEFAULT (datetime('now')),
    active INTEGER DEFAULT 1,
    FOREIGN KEY(company_id) REFERENCES companies(user_id)
);

-- Tabela habilidades requeridas para as vagas
CREATE TABLE job_skills (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    job_id INTEGER,
    skill_name TEXT,
    level_required INTEGER,
    FOREIGN KEY(job_id) REFERENCES jobs(id)
);

-- Tabela candidaturas às vagas
CREATE TABLE job_applications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    job_id INTEGER,
    candidate_id INTEGER,
    applied_at TEXT DEFAULT (datetime('now')),
    status TEXT DEFAULT 'pending',
    FOREIGN KEY(job_id) REFERENCES jobs(id),
    FOREIGN KEY(candidate_id) REFERENCES candidates(user_id)
);

-- Tabela pontuações de adequação às vagas
CREATE TABLE job_fit_scores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    job_id INTEGER,
    candidate_id INTEGER,
    technical_score REAL,
    cultural_score REAL,
    total_score REAL,
    created_at TEXT DEFAULT (datetime('now')),
    FOREIGN KEY(job_id) REFERENCES jobs(id),
    FOREIGN KEY(candidate_id) REFERENCES candidates(user_id)
);

-- Tabela recomendações de vagas para candidatos
CREATE TABLE job_recommendations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    candidate_id INTEGER,
    job_id INTEGER,
    score REAL,
    created_at TEXT DEFAULT (datetime('now')),
    FOREIGN KEY(candidate_id) REFERENCES candidates(user_id),
    FOREIGN KEY(job_id) REFERENCES jobs(id)
);