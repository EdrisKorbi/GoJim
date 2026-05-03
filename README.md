# 💪 GoJim - Gym Management System

A desktop gym management application built with Java Swing and MySQL.

## 📌 Table of Contents
- [Overview](#-overview)
- [Features](#-features)
  - [Dashboard](#-dashboard)
  - [Members Management](#-members-management)
  - [Subscriptions (Plans)](#-subscriptions-plans)
  - [Trainers Management](#️-trainers-management)
  - [Payments](#-payments)
- [Business Logic](#-business-logic)
- [Technical Details](#-technical-details)
  - [Architecture](#-architecture)
  - [UI Design](#-ui-design)
  - [Navigation System](#-navigation-system)
  - [Database Schema](#-database-schema)
- [Setup Instructions](#️-setup-instructions)
- [Default Login](#-default-login)
- [Future Improvements](#-future-improvements)
- [Author](#-author)
- [License](#-license)

## 📌 Overview
**GoJim** is a desktop gym management application built using **Java Swing** and **MySQL**.
It provides an intuitive dashboard to manage members, subscriptions, trainers, and payments with real-time updates.

## 🚀 Features

### 📊 Dashboard
- Real-time statistics:
  - Total revenue
  - Members count
  - Active members
  - Trainers count
  - Daily visits
- Gym capacity indicator
- Recent members preview
- Quick navigation via clickable cards

### 👤 Members Management
- Add, edit, delete members
- Search/filter members
- Automatic status handling:
  - **Pending** → payment not completed
  - **Active** → payment completed
  - **Expired** → subscription ended

### 📦 Subscriptions (Plans)
- Create, edit, delete plans
- Define:
  - Duration (days)
  - Price
- Used when registering members

### 🏋️ Trainers Management
- Add, edit, delete trainers
- Search trainers
- Store:
  - Specialty
  - Experience

### 💳 Payments
- Track all member payments
- Update payment status:
  - Pending → Paid
- Automatic synchronization with member status
- Revenue calculation (paid & pending)

## 🧠 Business Logic

- When a member is created:
  - Status = **Pending**
  - A **payment record is automatically created**
- When payment is marked **Paid**:
  - Member becomes **Active**
- When subscription expires:
  - Member becomes **Expired**

## 🏗️ Technical Details

### Architecture

```text
UI (Swing Panels & Components)
        ↓
DAO Layer (Database Logic)
        ↓
DatabaseConnection (JDBC)
        ↓
MySQL Database

Structure

com.gojim
├── config/        → Database connection
├── dao/           → Data access logic
├── model/         → Data models
├── ui/
│   ├── panels/    → Main pages
│   └── components/→ Reusable UI components
```

### 🧩 UI Design
Built with Java Swing
Custom components:
    Rounded panels
    Styled buttons
    Custom dropdowns
    Stat cards
Dark theme with consistent color system (AppColors)
Single-window navigation using CardLayout

### 🔄 Navigation System

The app uses CardLayout:
One window → multiple panels → switch dynamically
Example:
cardLayout.show(contentPanel, "Members");

### 🗄️ Database Schema

Tables:
    subscriptions
    members
    trainers
    payments

Relationships:
subscriptions → members → payments

## ⚙️ Setup Instructions

1. Clone the repository
    `git clone https://github.com/YOUR_USERNAME/gojim.git`
    `cd gojim`
2. Setup MySQL
    Open phpMyAdmin or MySQL
    Import `schema.sql`
3. Configure database connection
    Edit:
        `DatabaseConnection.java`
    Set:
        `URL`
        `USERNAME`
        `PASSWORD`
4. Run the application
    Run `Main.java`

## 🔐 Default Login
    Username: `admin`
    Password: `admin`

## 🎯 Future Improvements
- Role-based authentication (admin/user)
- Charts for analytics
- Export data (PDF/Excel)
- REST API backend
- Multi-user support

## 👨‍💻 Author
- Edris Korbi
- Computer Science Student

## 📄 License
This project is for educational purposes.
