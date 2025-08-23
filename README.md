# StartHub

**Version:** 1.0
**Developers:** Essa Al Ghanim - Faisal Al Khrayef - Yasir Alateeq

**Tech Stack:**

* **Backend:** Java 17, Spring Boot
* **Database:** MySQL (AWS RDS)
* **ORM:** Hibernate with JPA
* **API & Integrations:** REST APIs, OpenAI, Moyasar (Payments + Webhook)
* **Tools:** Ngrok, Postman, Maven
* **Deployment:** AWS (Elastic Beanstalk, ECS)

---

## Overview

StartHub is a digital platform designed to empower Saudi entrepreneurs and early-stage startups. It provides structured guidance, advisory services, team-building opportunities, and growth support — all in one place.

Key focus for this version: **Idea validation, consultancy, payments, and team building.**

---

## Core Modules & Endpoints I worked on

| Module         | Endpoint                   | Description                              |
| -------------- | -------------------------- | ---------------------------------------- |
| **AI Advisor** | `validateBusinessIdea`     | Validate startup ideas                   |
|                | `analyzeMarket`            | Analyze market trends and opportunities  |
|                | `getCompetitiveAnalysis`   | Check competitors in the market          |
|                | `getRevenueModelAdvice`    | Guidance on revenue strategies           |
|                | `getFundingStrategy`       | Advice on fundraising and investors      |
|                | `getFinancialPlanning`     | Financial planning for startups          |
|                | `getMvpStrategy`           | MVP development strategy                 |
|                | `getGrowthStrategy`        | Growth and scaling guidance              |
|                | `getTeamBuildingAdvice`    | Advice on building effective teams       |
|                | `getLegalStructure`        | Guidance on legal setup and structure    |
|                | `getRiskAssessment`        | Evaluate potential risks                 |
|                | `getGeneralAdvice`         | General startup advice                   |
| **Payment**    | `processPayment`           | Initiate payments                        |
|                | `payForProject`            | Pay for a freelancer project             |
|                | `payForSession`            | Pay for an advisor session               |
|                | `payForSubscription`       | Pay for platform subscription            |
|                | `handleCallback`           | Payment gateway webhook handling         |
|                | `getPayment`               | Retrieve payment details                 |
|                | `cancelSubscription`       | Cancel an active subscription            |
|                | `getSubscriptionStatus`    | Check current subscription status        |
|                | `getExpiringSubscriptions` | List subscriptions about to expire       |
| **Startup**    | `addFounderToStartup`      | Add a founder or co-founder to a startup |

---

## APIs Used

| API             | Purpose                                                  |
| --------------- | -------------------------------------------------------- |
| OpenAI          | AI advisor, business idea validation, insights, guidance |
| Moyasar         | Payment processing                                       |
| Moyasar Webhook | Payment callbacks and subscription handling              |

---

## Entities

* **User:** Founder, Investor, Advisor, Freelancer
* **Startup (Company)**
* **AdvisorSession**
* **FreelancerProject**
* **Investment**
* **Payment**
* **Subscription**

---

## DB Diagram
<img width="571" height="851" alt="image" src="https://github.com/user-attachments/assets/8dfc12e7-7ab3-4bdb-8bdd-f8aaabb12569" />


---

## Notes

* Payment and subscription handling fully integrated with Moyasar.
* AI-powered guidance leverages OpenAI APIs for personalized advice.
* Endpoints are RESTful and tested via Postman.
* Documented using Swagger.


