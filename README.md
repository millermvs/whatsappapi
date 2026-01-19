
## 📌 Visão Geral

Projeto backend desenvolvido em **Java + Spring Boot (starter-web)** para integração com a **API oficial do WhatsApp Cloud (Meta)**.

O sistema é responsável por:

- 📩 Receber mensagens e eventos via **Webhook**
- 📤 Enviar mensagens via **Graph API**
- 🔐 Centralizar tokensss, segurança e regras de negócio no **backend**
- 🔄 Servir como base para aplicações **SaaS** (ex.: clínicas, empresas)

> ⚠️ O frontend **nunca** se comunica diretamente com a Meta.  
> Toda integração externa é responsabilidade do backend.

---

## 🧱 Arquitetura (Visão Geral)

modules/whatsapp
  ├─ application
  │   └─ controllers
  ├─ domain
  │   ├─ services
  │   ├─ models
  │   └─ gateways
  └─ infrastructure
      └─ meta
          ├─ client
          ├─ adapter
          ├─ dto
          └─ mapper

---