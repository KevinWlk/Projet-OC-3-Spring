
# ChaTop API 🇫🇷 | 🇬🇧
## 🇬🇧 ChaTop API

### Description

The ChaTop API serves as the server for the ChaTop application. Developed with Spring Boot 3 and Java 17, it incorporates Spring-doc OpenAPI and Swagger UI for comprehensive documentation.

### Features

The ChaTop API includes the following key features:

- User Authentication with **JWT** (utilizing the io.jsonwebtoken library)
- User registration
- Rental creation
- Rental display
- Rental update
- Sending messages

### Installation

Before running the ChaTop Backend API, follow these installation steps:

1. Clone this repository:  
   `git clone https://github.com/KevinWlk/Projet-OC-3-Spring.git`
2. Install JDK 17 (Java Development Kit) on your local machine.
3. Install Maven locally.
4. Install MySQL on your local machine and create a database for the application.
### SQL Script

Create the database and tables using this SQL script:

```sql
CREATE DATABASE chatop;

USE chatop;

create table images ( 
id bigint auto_increment primary key, 
bytes mediumblob null, 
name varchar(255) null, 
type varchar(255) null );

create table users ( 
id int auto_increment primary key, 
created_at date null, 
email varchar(255) not null, 
name varchar(255) not null, 
password varchar(255) not null, 
updated_at date null );

create table rentals ( 
id int auto_increment primary key, 
created_at date null, 
description varchar(2000) not null, 
name varchar(255) not null, 
picture varchar(255) not null, 
price int not null, 
surface int not null, 
updated_at date null, 
owner_id int not null, 
constraint UKgev8r1mct7oy6h9x0oi7nvic3 unique (name), 
constraint FKf462yhxa9vd3m2qdmcoixg1fv foreign key (owner_id) references users (id) );

create table messages ( 
id int auto_increment primary key, 
created_at date null, 
message varchar(2000) null, 
updated_at date null, 
rental_id int null, 
user_id int null, 
constraint FK3ce1i9w1rtics9wjwj8y5y3md foreign key (rental_id) references rentals (id), 
constraint FKpsmh6clh3csorw43eaodlqvkn foreign key (user_id) references users (id) );
```

5. Configure the `application.properties` file with the following information:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chatop
spring.datasource.username=root
spring.datasource.password=YourPassword
```

6. Run the application with Maven:
   ```bash
   mvn clean install spring-boot:run
   ```

### Launch the Front-end project

To launch the front-end project locally:  
[Develop the back-end using Java and Spring](https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring)

```bash
npm install
npm run start
```

### Testing with Swagger UI

- Run the ChaTop application.
- Access Swagger UI documentation at the following address:  
  `http://localhost:3001/swagger-ui/index.html`
- Register a user by sending a POST request to the `/api/auth/register` endpoint.
- Log in by sending a POST request to the `/api/auth/login` endpoint.
- Copy the JWT token from the response.
- Click on the `Authorize` button on the top-right corner of the Swagger UI page.
- Paste the JWT token in the `Value` field and click on `Authorize`.
- You can now test the API endpoints.

---

## 🇫🇷 ChaTop API

### Description

L'API ChaTop sert de serveur pour l'application ChaTop. Développée avec Spring Boot 3 et Java 17, elle utilise Spring-doc OpenAPI et Swagger UI pour une documentation complète.

### Fonctionnalités

L'API ChaTop comprend les fonctionnalités suivantes :

- Authentification des utilisateurs avec **JWT** (utilisant la bibliothèque io.jsonwebtoken)
- Inscription des utilisateurs
- Création de locations
- Affichage des locations
- Mise à jour des locations
- Envoi de messages

### Installation

Avant d'exécuter l'API ChaTop Backend, suivez ces étapes d'installation :

1. Clonez ce dépôt :  
   `git clone https://github.com/KevinWlk/Projet-OC-3-Spring.git`
2. Installez JDK 17 (Java Development Kit) sur votre machine locale.
3. Installez Maven localement.
4. Installez MySQL sur votre machine locale et créez une base de données pour l'application.
### Script SQL

Créez la base de données et les tables nécessaires avec ce script SQL :

```sql
CREATE DATABASE chatop;

USE chatop;

create table images ( 
id bigint auto_increment primary key, 
bytes mediumblob null, 
name varchar(255) null, 
type varchar(255) null );

create table users ( 
id int auto_increment primary key, 
created_at date null, 
email varchar(255) not null, 
name varchar(255) not null, 
password varchar(255) not null, 
updated_at date null );

create table rentals ( 
id int auto_increment primary key, 
created_at date null, 
description varchar(2000) not null, 
name varchar(255) not null, 
picture varchar(255) not null, 
price int not null, 
surface int not null, 
updated_at date null, 
owner_id int not null, 
constraint UKgev8r1mct7oy6h9x0oi7nvic3 unique (name), 
constraint FKf462yhxa9vd3m2qdmcoixg1fv foreign key (owner_id) references users (id) );

create table messages ( 
id int auto_increment primary key, 
created_at date null, 
message varchar(2000) null, 
updated_at date null, 
rental_id int null, 
user_id int null, 
constraint FK3ce1i9w1rtics9wjwj8y5y3md foreign key (rental_id) references rentals (id), 
constraint FKpsmh6clh3csorw43eaodlqvkn foreign key (user_id) references users (id) );
```
5. Configurez le fichier `application.properties` avec les informations suivantes :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chatop
spring.datasource.username=root
spring.datasource.password=VotreMotDePasse
```

6. Lancez l'application avec Maven :
   ```bash
   mvn clean install spring-boot:run
   ```

### Lancer le projet front-end

Pour lancer le projet localement (Front) :  
[Développez le back-end en utilisant Java et Spring](https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring)

```bash
npm install
npm run start
```

### Tester avec Swagger UI

- Exécutez l'application ChaTop.
- Accédez à la documentation Swagger UI à l'adresse suivante :  
  `http://localhost:3001/swagger-ui/index.html`
- Inscrivez un utilisateur en envoyant une requête POST à l'endpoint `/api/auth/register`.
- Connectez-vous en envoyant une requête POST à l'endpoint `/api/auth/login`.
- Copiez le token JWT depuis la réponse.
- Cliquez sur le bouton `Authorize` dans le coin supérieur droit de la page Swagger UI.
- Collez le token JWT dans le champ `Value` et cliquez sur `Authorize`.
- Vous pouvez maintenant tester les autres endpoints de l'API.

---

