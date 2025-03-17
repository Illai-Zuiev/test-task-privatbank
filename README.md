# Service One

## Опис
Мікросервіс **Service One** приймає HTTP-запити з JSON-тілом, що містить умовний `clientId`. Також він перевіряє авторизацію за допомогою заголовка `sid`. Після успішної авторизації дані відправляються через ActiveMQ у JMS-чергу `client.id.queue`.

## Функціональність
- Swagger http://localhost:8080/swagger-ui/index.html#/client-controller/receiveClient.
- Прийом HTTP POST запитів за адресою `/client`.
- Перевірка заголовка `sid` на валідність.
- Сериалізація вхідного JSON (ClientRequest) і відправка його у JMS-чергу `client.id.queue`.

## Використані технології
- **Spring Boot 3** (Java 17)
- **ActiveMQ** (JMS)
- **Maven**
- **Docker** (Dockerfile та docker-compose)

## Налаштування
- **Порт:** 8080
- **JMS брокер:** ActiveMQ (конфігурація в `application.yml`)

# Service Two

## Опис
Мікросервіс **Service Two** отримує повідомлення з JMS-черги `client.id.queue`, де міститься JSON з полем `clientId`. Він імітує виклик стороннього сервісу для отримання імені клієнта, додає поле `name` у отриманий об’єкт і відправляє оновлене повідомлення у JMS-чергу `client.name.queue`.

## Функціональність
- Отримання повідомлень з черги `client.id.queue`.
- Ручна десериалізація JSON у об’єкт `ClientData`.
- Додавання поля `name` (імітація отримання даних).
- Сериалізація об’єкта і відправка у чергу `client.name.queue`.

## Використані технології
- **Spring Boot 3** (Java 17)
- **ActiveMQ** (JMS)
- **Jackson (ObjectMapper)**
- **Maven**
- **Docker**

## Налаштування
- **Черга вхідних повідомлень:** `client.id.queue`
- **Черга вихідних повідомлень:** `client.name.queue`

# Service Three

## Опис
Мікросервіс **Service Three** отримує повідомлення з JMS-черги `client.name.queue`, де міститься JSON з полями `clientId` та `name`. Він імітує отримання даних про адресу проживання, додає поле `address` у об’єкт і відправляє оновлене повідомлення у JMS-чергу `client.address.queue`.

## Функціональність
- Отримання повідомлень з черги `client.name.queue`.
- Ручна десериалізація JSON у об’єкт `ClientData`.
- Додавання поля `address` (імітація отримання адреси).
- Сериалізація об’єкта і відправка у чергу `client.address.queue`.

## Використані технології
- **Spring Boot 3**
- **ActiveMQ** (JMS)
- **Jackson**
- **Maven**
- **Docker**

## Налаштування
- **Черга вхідних повідомлень:** `client.name.queue`
- **Черга вихідних повідомлень:** `client.address.queue`

# Service Four

## Опис
Мікросервіс **Service Four** отримує повідомлення з JMS-черги `client.address.queue`, де міститься JSON з полями `clientId`, `name` та `address`. Він імітує отримання контактних даних і номерів карток, додає відповідні поля (`contacts`, `cardNumbers`) і відправляє оновлене повідомлення у JMS-чергу `client.contact.queue`.

## Функціональність
- Отримання повідомлень з черги `client.address.queue`.
- Ручна десериалізація JSON у об’єкт `ClientData`.
- Додавання полів `contacts` (імітація отримання даних).
- Сериалізація об’єкта і відправка у чергу `client.contact.queue`.

## Використані технології
- **Spring Boot 3**
- **ActiveMQ** (JMS)
- **Jackson**
- **Maven**
- **Docker**

## Налаштування
- **Черга вхідних повідомлень:** `client.address.queue`
- **Черга вихідних повідомлень:** `client.contact.queue`

# Service Five

## Опис
Мікросервіс **Service Five** отримує повідомлення з JMS-черги `client.contact.queue`, де міститься JSON з повними даними клієнта (поля: `clientId`, `name`, `address`, `contacts`, `cardNumbers`). Він десериалізує повідомлення, перетворює його в сутність для збереження та записує дані в SQL базу даних (SQLite) за допомогою JPA.

## Функціональність
- Отримання повідомлень з черги `client.contact.queue`.
- Ручна десериалізація JSON у об’єкт `ClientData`.
- Перетворення даних у сутність `ClientEntity`.
- Збереження даних у базу даних за допомогою JPA.
- Інденпотентність (базова).

## Використані технології
- **Spring Boot 3**
- **ActiveMQ** (JMS)
- **Spring Data JPA**
- **PostgreSQL**
- **Jackson**
- **Maven**
- **Docker**

## Налаштування
- **Черга вхідних повідомлень:** `client.contact.queue`
- **База даних:** конфігурація в `application.properies`
- **Порт сервісу:** 8084