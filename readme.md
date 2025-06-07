# 🚛 Supply Chain Management System (SCMS)

A modular, microservices-based backend built with **Spring Boot**, designed for managing modern supply chain operations such as supplier onboarding, product and inventory management, order tracking, facility control, transportation logistics, and event-driven notifications via Kafka.

---

## 🧱 Microservices Overview

```

SupplyChainManagementSys/
├── auth-server                    # Handles login/authentication (JWT)
├── config-server                  # Centralized configuration (Spring Cloud Config)
├── eureka-server                  # Service discovery
├── gateway-server                 # API Gateway for routing
├── common-files                   # Shared DTOs, constants, exceptions
│
├── supplier-server                # Supplier onboarding and management
├── product-server                 # Product entity management
├── productManagement-server       # Product logic and control
├── inventory-server               # Inventory quantity, alerts
├── order-server                   # Order entity
├── ordermanagement-server         # Full order lifecycle logic
├── deliverymanagement-server      # Coordinates delivery flow
├── trip-planning-server           # Route and ETA planning
├── driver-server                  # Driver onboarding and scheduling/ Delivery entity management
├── facility-server                # Warehouse and facility control
├── notification-server            # Kafka consumer for notifications
├── webhook-server                 # External system integration
├── infra                          # Docker and DevOps (optional)
├── bin                            # Executables/scripts
├── pom.xml                        # Maven parent file
└── README.md                      # This file

````

---

## ⚙️ Technologies Used

| Category        | Technology                     |
|----------------|---------------------------------|
| Backend         | Spring Boot                    |
| Configuration   | Spring Cloud Config            |
| Discovery       | Eureka                         |
| API Gateway     | Spring Cloud Gateway           |
| Messaging       | Apache Kafka                   |
| Auth            | Spring Security + JWT          |
| DB & Cache      | PostgreSQL + Redis             |
| Build Tool      | Maven                          |
| Containerization| Docker, Docker Compose         |

---

## 🐳 Docker Compose Setup

Docker is used to spin up dedicated PostgreSQL and Redis instances for each microservice, plus pgAdmin for GUI-based database access.

### 📦 Services Included

| Microservice            | PostgreSQL DB        | Redis Instance      | Port Mapping (DB:Redis) |
|-------------------------|----------------------|----------------------|--------------------------|
| `auth-server`           | `authDB`             | `my_redis` (shared)  | `5430:5432`, `6380:6379` |
| `supplier-server`       | `supplierDB`         | `redis_supplier`     | `5431:5432`, `6388:6379` |
| `facility-server`       | `facilityDB`         | `redis_facility`     | `5432:5432`, `6390:6379` |
| `inventory-server`      | `inventoryDB`        | `redis_inventory`    | `5429:5432`, `6385:6379` |
| `product-server`        | `productDB`          | `redis_product`      | `5433:5432`, `6392:6379` |
| `order-server`          | `orderDB`            | `redis_order`        | `5434:5432`, `6395:6379` |
| `driver-server`         | `driverDB`           | `redis_driver`       | `5435:5432`, `6397:6379` |
| `pgAdmin`               | GUI for all DBs      | —                    | `5050:80`                |

> All services are connected via a shared Docker network `allinone_net`.

---

### ▶️ How to Start

```bash
docker-compose up -d
````

Then visit [http://localhost:5050](http://localhost:5050) to access pgAdmin.

* **Email**: `pgadmin4@pgadmin.org`
* **Password**: `admin`

To connect a database:

* **Host**: e.g., `postgres_supplier`
* **Port**: e.g., `5432`, `5433`, ...
* **Username**: `root`
* **Password**: `1234`

---

## ✅ Module Highlights

* **Supplier Module**: Manage supplier profiles, factory info, NDA uploads.
* **Product Module**: Track development/published status; CRUD operations.
* **Order Module**: Hold, clone, cancel orders with business rules.
* **Inventory Module**: Auto alert when stock < threshold.
* **Driver Module**: Schedule drivers and track availability.
* **Trip Module**: Preloaded route planning and ETA calculations.
* **Notification Module**: Kafka consumer sends email alerts (stateless).

---

## 📬 Kafka Topics

| Topic Name           | Description                             |
| -------------------- | --------------------------------------- |
| `order.lateDelivery` | Delayed orders trigger notifications    |
| `inventory.lowStock` | Low inventory alert to manager/supplier |
| `driver.notArrived`  | Drivers late to location alerts         |

---

## 🔗 3rd Party Integration

`webhook-server` handles inbound connections from external systems (e.g., supplier API or logistics partner).

📄 [Integration Requirement Doc](https://drive.google.com/file/d/1BNYaF6SeE-1zwGNQM549-L4a5ll6EGTr/view?usp=drive_link)

---

## 👤 Author

Yihao Ai
Backend Engineer | Java + Spring Boot | Microservices

---

## 🪪 License

This project is open-sourced under the MIT License.

# SupplyChainManagementSys
