# Sumário

- [CANDIDATE](#candidate)
- [COMPANY](#company)
- [JOB APPLICATION](#job-application)
- [JOB](#job)
- [AUTH](#auth)
- [USER](#user)

---

# API Documentation — Synapsys

## Autenticação
A maioria dos endpoints exige:

```
Authorization: Bearer <token>
```

---

# CANDIDATE

## POST /candidate/create
Cria o perfil de candidato para o usuário autenticado. Cada usuário pode ter apenas 1 candidato.

### Headers
```
Authorization: Bearer <token>
```

### Body
```json
{
  "displayName": "string",
  "purpose": "string",
  "workStyle": "string",
  "interests": "string"
}
```

### Retornos
- 200 OK
- 400 Dados inválidos
- 400 Candidato já cadastrado
- 500 Erro interno

---

## PUT /candidate/update-profile
Atualiza somente os campos enviados.

### Headers
```
Authorization: Bearer <token>
```

### Body
```json
{
  "displayName": "string",
  "purpose": "string",
  "workStyle": "string",
  "interests": "string"
}
```

### Retornos
- 200 OK
- 404 Candidato não encontrado
- 500 Erro interno

---

## PUT /candidate/update-survey
Registra respostas do questionário comportamental.

### Headers
```
Authorization: Bearer <token>
```

### Body
```json
{
  "questions": [
    { "theAsk": "string", "answer": "string" }
  ]
}
```

### Retornos
- 200 OK
- 400 Questionário ausente
- 404 Candidato não encontrado
- 500 Erro interno

---

# CANDIDATE — EDUCATION

## POST /candidate/new-education
Adiciona nova formação educacional.

### Headers
```
Authorization: Bearer <token>
```

### Body
```json
{
  "institution": "string",
  "course": "string",
  "level": "string",
  "startDate": "string",
  "endDate": "string"
}
```

### Retornos
- 201 Criado
- 400 Dados inválidos
- 500 Erro interno

---

## PUT /candidate/update-education?institution=<string>&course=<string>
Atualiza registro específico.

### Retornos
- 200 OK
- 403 Sem permissão
- 404 Educação não encontrada
- 500 Erro interno

---

## GET /candidate/education
Lista todas as educações.

### Retornos
- 200 OK
- 404 Nenhum registro encontrado
- 500 Erro interno

---

## DELETE /candidate/delete-education?institution=<string>&course=<string>
Remove uma educação.

### Retornos
- 200 OK
- 403 Sem permissão
- 404 Não encontrado
- 500 Erro interno

---

# CANDIDATE — EXPERIENCE

## POST /candidate/new-experience
Adiciona experiência profissional.

### Body
```json
{
  "companyName": "string",
  "role": "string",
  "description": "string",
  "startDate": "string",
  "endDate": "string"
}
```

### Retornos
- 201 Criado
- 400 Dados inválidos
- 500 Erro interno

---

## PUT /candidate/update-experience?experienceId=<int>
Atualiza experiência específica.

### Retornos
- 200 OK
- 400 ID ausente
- 404 Experiência não encontrada
- 500 Erro interno

---

## GET /candidate/experiences
Lista experiências.

### Retornos
- 200 OK
- 404 Nenhuma encontrada
- 500 Erro interno

---

# CANDIDATE — SKILLS

## POST /candidate/new-skill
Adiciona habilidade.

### Body
```json
{
  "skillName": "string",
  "level": 1
}
```

### Retornos
- 201 Criado
- 400 Dados inválidos
- 409 Já cadastrada
- 500 Erro interno

---

## PUT /candidate/update-skill
Atualiza habilidade.

### Retornos
- 200 OK
- 404 Não encontrada
- 500 Erro interno

---

## GET /candidate/skills
Lista habilidades.

### Retornos
- 200 OK
- 404 Nenhuma habilidade
- 500 Erro interno

---

## DELETE /candidate/delete-skill?skillName=<string>
Remove habilidade.

### Retornos
- 200 OK
- 404 Não encontrada
- 500 Erro interno

---

# COMPANY

## POST /company/create
Cria empresa.

### Body
```json
{
  "name": "string",
  "description": "string",
  "industry": "string",
  "culture": "string"
}
```

### Retornos
- 201 Criado
- 400 Dados inválidos
- 500 Erro interno

---

## GET /company/me
Retorna empresa do usuário.

### Retornos
- 200 OK
- 404 Não encontrada
- 500 Erro interno

---

## GET /company/{name}
Busca empresa pelo nome.

### Retornos
- 200 OK
- 400 Nome inválido
- 404 Não encontrada
- 500 Erro interno

---

## PUT /company/update
Atualiza empresa.

### Retornos
- 200 OK
- 404 Não encontrada
- 500 Erro interno

---

## DELETE /company/delete
Remove empresa.

### Retornos
- 200 OK
- 500 Erro interno

---

# JOB APPLICATION

## POST /job/application/create
Cria candidatura.

### Body
```json
{
  "jobId": 1
}
```

### Retornos
- 200 OK
- 400 Dados inválidos
- 500 Erro interno

---

## GET /job/application/list
Lista candidaturas.

### Retornos
- 200 OK
- 500 Erro interno

---

## GET /job/application/get?id=<int>
Retorna candidatura.

### Retornos
- 200 OK
- 404 Não encontrada
- 403 Sem permissão
- 500 Erro interno

---

## PUT /job/application/update?id=<int>
Atualiza candidatura.

### Retornos
- 200 OK
- 404 Não encontrada
- 403 Sem permissão
- 500 Erro interno

---

## DELETE /job/application?id=<int>
Remove candidatura.

### Retornos
- 200 OK
- 404 Não encontrada
- 403 Sem permissão
- 500 Erro interno

---

# JOB

## POST /job/create
Cria vaga.

### Body
```json
{
  "title": "string",
  "description": "string",
  "salary": 0,
  "workModel": "string",
  "requiredSkills": "string"
}
```

### Retornos
- 200 OK
- 400 Dados inválidos
- 500 Erro interno

---

## GET /job/list
Lista vagas da empresa.

### Retornos
- 200 OK
- 500 Erro interno

---

## GET /job/all
Lista todas vagas.

### Retornos
- 200 OK
- 500 Erro interno

---

## PUT /job/update?id=<int>
Atualiza vaga.

### Retornos
- 200 OK
- 404 Não encontrada
- 403 Sem permissão
- 500 Erro interno

---

## DELETE /job/delete?id=<int>
Remove vaga.

### Retornos
- 200 OK
- 404 Não encontrada
- 403 Sem permissão
- 500 Erro interno

---

## GET /job/scoreboard?jobId=<int>
Lista JobFitScores.

### Retornos
- 200 OK
- 404 Não encontrada
- 500 Erro interno

---

# AUTH

## POST /login
Autentica usuário.

### Body
```json
{
  "email": "string",
  "password": "string"
}
```

### Retornos
- 200 OK + token
- 401 Unauthorized
- 500 Erro interno

---

# USER

## POST /user/register
Cria usuário.

### Body
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "role": "candidate | company"
}
```

### Retornos
- 201 Created
- 400 Bad Request
- 409 Conflict
- 500 Internal Server Error

---

## DELETE /user/{id}
Apenas admin.

### Retornos
- 200 OK
- 403 Forbidden
- 500 Internal Server Error

---

## DELETE /user/delete-account
Usuário deleta a própria conta.

### Retornos
- 200 OK
- 403 Forbidden
- 500 Internal Server Error
