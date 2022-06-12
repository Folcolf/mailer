# Mailer

## How to get started

### Install

1. Java 18
2. Get the newest jar

### Get authentication

1. Go to your gmail account [https://myaccount.google.com/](https://myaccount.google.com/)
2. Go to "Security"
3. Go to "Signing in to Google"
4. Click "App passwords"
5. Select app "Mail"
6. Select device "Other"
7. Tap "Mailer"
8. Copy the password in a .env file like this:

  ```properties
  username=<your email>
  password=<your password>
  ```

### Send a mail



  ```bash
 java -jar target/AutoMailer-<version>.jar -f=<csv file> -s"<subject>" -a=<attachemet>,<attachemet>,...
 ```
 