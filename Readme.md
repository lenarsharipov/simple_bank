**Application Launch:**
1. Download the archive and open it in an IDE (IntelliJ IDEA).
2. Start Docker.
3. In the IDE terminal, run the command `docker-compose up`.

**Test Task:**
You need to develop a service for "banking" operations. In our system, there are users (clients), and each client has exactly one "bank account" with an initial balance. Money can be transferred between clients. Interest is also accrued on the balance.

**Functional Requirements:**
- The system has users, and each user has exactly one "bank account". A user must also have a phone and email. At least one of them (phone or email) must be present.
- The "bank account" must have an initial balance. Additionally, the user must have a date of birth and full name specified.
- For simplicity, the system will have no roles—only regular clients.
- There should be an admin API (publicly accessible) through which new users can be created in the system by specifying a username, password, initial balance, phone, and email (username, phone, and email must be unique).
- The client’s account balance cannot go negative under any circumstances.
- The user can add/change their phone number and/or email, provided they are not already in use by other users.
- The user can delete their phone and/or email, but the last one cannot be removed.
- The rest of the user’s data cannot be changed.

**Search API:**
- You should be able to search for any client. The search should support filtering, pagination, and sorting. Filters:
  - If a date of birth is provided, filter the records where the date of birth is greater than the one in the request.
  - If a phone number is provided, filter for exact matches.
  - If a full name is provided, apply a 'like' filter in the format `'{text-from-request-param}%'`.
  - If an email is provided, filter for exact matches.
- Access to the API should be authenticated (except for the admin API for creating new users).

**Balance Update:**
Every minute, each client’s balance increases by 5%, but no more than 207% of the initial deposit. For example:
- Initial: 100, updated: 105.
- Initial: 105, updated: 110.25.

**Money Transfer:**
Implement the functionality to transfer money from one account to another. The transfer should be from an authenticated user’s account to another user's account. All necessary validations should be in place, and it should be thread-safe.

**Non-functional Requirements:**
- Add OpenAPI/Swagger.
- Add logging.
- Authentication via JWT.
- Write tests to cover the money transfer functionality.
