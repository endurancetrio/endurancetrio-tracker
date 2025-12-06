# Development Guide

This document covers the deployment workflow for the **EnduranceTrio Tracker** project. For
an overview of the project, see the [main README.md](../README.md).

## Table of Contents

1. [Container Architecture](#container-architecture)
2. [Server Setup](#server-setup)
3. [Reverse Proxy Setup](#reverse-proxy-setup)
4. [SSL Certificate](#ssl-certificate)

The **EnduranceTrio Tracker** REST API is optimized for containerized deployments using
[Docker](https://www.docker.com/). The project includes a robust CI/CD pipeline
that automatically builds and publishes OCI-compliant images to the GitHub
Container Registry ([GHCR](https://github.com/orgs/endurancetrio/packages)).

### Container Architecture

The deployment uses a lightweight Alpine-based image (`eclipse-temurin:21-jre-alpine`) optimized
for security and performance.

Key features of the container setup include:
- **Multi-stage Build**: Minimizes image size by separating the build environment from the runtime.
- **Non-root Execution**: Runs as a dedicated non-root user for security.
- **PUID/PGID Mapping**: A custom entrypoint script allows mapping the internal container user
  to a host user, ensuring seamless volume permission management for logs and data.
- **Health Checks**: Built-in integration with Spring Boot Actuator for orchestration health
  monitoring.

Official images are available at:

`ghcr.io/endurancetrio/endurancetrio-tracker`

| Tag Format    | Description                                      | Use Case        |
|---------------|--------------------------------------------------|-----------------|
| `X.Y.Z`       | Semantic version release (e.g., `1.0.0`)         | **Production**  |
| `sha-XXXXXXX` | Short Git SHA commit hash                        | Testing/Staging |

### Server Setup

The recommended way to deploy the application is using Docker Compose. To create the necessary
folders for the app installation, execute the following command in the deployment server:

```shell
sudo mkdir -p /opt/endurancetrio-tracker/logs
```

We will create a user to manage the **EnduranceTrio Tracker** application and set it as the owner
of the folder `/opt/endurancetrio-tracker/logs/`. This will be achieved with the execution of the
following commands:

```shell
sudo useradd -r -s /usr/sbin/nologin endurancetrio
sudo chown -R endurancetrio:endurancetrio /opt/endurancetrio-tracker/logs/
```

To confirm that the folder `/opt/endurancetrio-tracker/logs/` has the correct ownership,
check the output of the following command:

```shell
ls -lag
```

The folder `/opt/endurancetrio-tracker` will store the files necessary to deploy the application
with Docker Compose. To download, from this repository to the server, the `docker-compose.yaml` file
and the template for the `.env` file, execute the following commands:

```shell
cd /opt/endurancetrio-tracker/
sudo wget https://raw.githubusercontent.com/EnduranceCode/endurancetrio-tracker/refs/heads/tracker/docker/deployment/docker-compose.yaml
sudo wget https://raw.githubusercontent.com/EnduranceCode/endurancetrio-tracker/refs/heads/tracker/docker/deployment/.env-template
```

To confirm that the files were downloaded, check the output of the following command:

```shell
ls -lag
```

Check the content of the `docker-compose.yaml` file with the below command, and if necessary, use
the [nano text editor](https://www.nano-editor.org/) to introduce the necessary adaptations.

```shell
cat docker-compose.yaml
```

Create a `.env` file in the deployment folder, based on the provided `.env-template`, using
the following command:

```shell
sudo mv .env-template .env
```

The `.env` file manages environment-specific configurations and secrets.

**Key Environment Variables:**

| Variable                 | Description                                        | Required |
|--------------------------|----------------------------------------------------|----------|
| `VERSION`                | The image tag to deploy (e.g., `v1.0.0`)           | Yes      |
| `PUID`                   | User ID under which the container process runs     | Yes      |
| `PGID`                   | Group ID under which the container process runs    | Yes      |
| `TRACKER_EXT_PORT`       | The host port mapped to the API (e.g., `8080`)     | Yes      |
| `SPRING_PROFILES_ACTIVE` | Spring profile (e.g., `dev` or `prod`)             | Yes      |
| `DB_URL`                 | Datasource URL                                     | Yes      |
| `DB_USERNAME`            | Database username                                  | Yes      |
| `DB_SECRET`              | Database password                                  | Yes      |
| `FIRST_OWNER`            | Name for the initial account initialization        | Optional |
| `FIRST_HASH`             | Bcrypt hash for the initial account initialization | Optional |

The user ID of the created `endurancetrio` user is obtained with the following command:

```shell
id -u endurancetrio
```

The group ID of the created `endurancetrio` user is obtained with the following command:

```shell
id -g endurancetrio
```

Open the `.env` file with the [nano text editor](https://www.nano-editor.org/)
and set the environment variables values. After setting the values for all variables,
save the file with the command `CTRL + O` and then close the editor with the command `CTRL + X`.

Given the sensitive nature of the variable `FIRST_HASH`, its value should be provided as a temporary
environment variable rather than persisted in the `.env` file to reduce attack surface.

> **Security Note**
>
> Refer to the [API Key Management](./development.md#api-key-management) section for details
> on generating the `FIRST_HASH`.

To complete deployment of **EnduranceTrio Tracker** REST API, execute the following command:

```shell
docker compose -p endurancetrio-tracker up -d
```

The output of the above command should show that **EnduranceTrio Tracker** REST API was deployed
with success. For a second confirmation, replace the placeholders in the below commands as
appropriate, and check its output.

```shell
docker ps
docker logs endurancetrio-tracker
curl -f http://localhost:{TRACKER_EXT_PORT}/actuator/health
```

> **Placeholder Definition**
>
> + **{TRACKER_EXT_PORT}** : The external port on the Docker host for accessing the application

### Reverse Proxy Setup

To ensure that the necessary [Apache Server](https://httpd.apache.org/) modules for reverse
proxying are enabled, execute the following commands:

```shell
sudo a2enmod proxy
sudo a2enmod proxy_http
sudo a2enmod headers
sudo a2enmod rewrite
sudo systemctl reload apache2
```

To have a domain (or a subdomain) pointing to your **EnduranceTrio Tracker** REST API instance,
you need to start by [creating the DNS records](https://docs.digitalocean.com/products/networking/dns/how-to/manage-records/)
of the desired domain (or subdomain) redirecting to your server's IP address.

After creating the necessary [DNS Records](https://docs.digitalocean.com/products/networking/dns/),
create an Apache Virtual Host configuration file. This repository includes a template that sets
the following redirects:

- `domain` -> `localhost:<PORT>/`
- `api subdomain` -> `localhost:<PORT>/api/`
- `openapi subdomain` -> `localhost:<PORT>/swagger-ui/index.html`

To use the [provided template](../apache/vhost-template.conf), execute the following command:

```shell
sudo wget -P /etc/apache2/sites-available/ https://raw.githubusercontent.com/EnduranceCode/endurancetrio-tracker/refs/heads/tracker/apache/vhost-template.conf
```

Then, replace the placeholder in the below command as appropriate and execute it to rename the file.

```shell
sudo mv /etc/apache2/sites-available/vhost-template.conf /etc/apache2/sites-available/{SECOND_LEVEL_DOMAIN_SLD}.conf
```

> **Placeholder Definition**
>
> + **{SECOND_LEVEL_DOMAIN_SLD}** : The [Second-level domain](https://en.wikipedia.org/wiki/Second-level_domain) (e.g., 'example' in example.com)

Customize the Virtual Host configuration file downloaded with the previous command, using
[nano text editor](https://www.nano-editor.org/), and replace the included placeholders
as described in the following table:

| Placeholder               | Description                                                  |
|---------------------------|--------------------------------------------------------------|
| <SECOND_LEVEL_DOMAIN_SLD> | Domain name (e.g., 'example' in example.com)                 |
| <TOP_LEVEL_DOMAIN_TLD>    | Top-level domain (e.g., 'com', 'org', 'net')                 |
| <SERVER_ADMIN_EMAIL>      | Administrator email for server notifications                 |
| <TRACKER_EXT_PORT>        | External port where your tracker app runs (e.g., 8080, 8081) |

Check if it's necessary any further modifications, implement it if necessary and when finished,
save the file with the command `CTRL + O` and then exit the text editor with the command `CTRL + X`.

Validate the Apache Server configuration with the following command:

```shell
sudo apachectl configtest
```

To activate the new Virtual Host, replace the placeholder in the below commands as appropriate
and then execute it.

```shell
sudo a2ensite {SECOND_LEVEL_DOMAIN_SLD}.conf
sudo systemctl reload apache2
```

> **Placeholder Definition**
>
> + **{SECOND_LEVEL_DOMAIN_SLD}** : The [*Second-level domain*](https://en.wikipedia.org/wiki/Second-level_domain) (e.g., 'example' in example.com)

If the domain of the Virtual Host created with the previous procedure has already its DNS Records
pointing to the server's IP address, enter the below URL into a browser’s address bar to test
the new local Virtual Host.

```text
http://{SECOND_LEVEL_DOMAIN_SLD}.{TOP_LEVEL_DOMAIN_TLD}
```

> **Placeholder Definition**
>
> + **{SECOND_LEVEL_DOMAIN_SLD}** : The [*Second-level domain*](https://en.wikipedia.org/wiki/Second-level_domain) (e.g., 'example' in example.com)
> + **{TOP_LEVEL_DOMAIN_TLD}**    : The [TLD](https://en.wikipedia.org/wiki/Top-level_domain) (e.g., 'com', 'org', 'net')

### SSL Certificate

If [Certbot](https://certbot.eff.org/instructions?ws=apache&os=ubuntufocal) isn't yet installed
on you server, install it and set the SSL certificate for the **EnduranceTrio Tracker** REST API
instance domain (or a subdomain) following the [instructions available here](https://github.com/EnduranceCode/server-setup-guide/blob/master/03-01-apache-server-management.md#312-apache--secure-apache-with-lets-encrypt).
If you already have SSL Certificates installed on your server with
[Certbot](https://certbot.eff.org/instructions?ws=apache&os=ubuntufocal), you can expand it
to include the new domain, or you can create a separate certificate for the new domain.

To expand an existing certificate, execute the following command:

```shell
sudo certbot --apache --cert-name {EXISTING_DOMAIN} --expand -d {EXISTING_DOMAIN} -d {NEW_DOMAIN}
```

> **Placeholder Definition**
>
> + **{EXISTING_DOMAIN}** : The existing domain (or subdomain) that already has a SSL certificate
> + **{NEW_DOMAIN}**      : The new domain (or subdomain) to be included in the existing SSL certificate

Otherwise, to create a separate certificate for the new domain (or subdomain), execute the following
command:

```shell
sudo certbot --apache -d {DOMAIN}
```

> **Placeholder Definition**
>
> + **{DOMAIN}** : The domain (or subdomain) of the new SSL certificate

[Certbot](https://certbot.eff.org/instructions?ws=apache&os=ubuntufocal) will create a file,
at `/etc/apache2/sites-available/` named `{SECOND_LEVEL_DOMAIN_SLD}-le-ssl.conf` and it's necessary
to check if the `RequestHeader` directives are set correctly. Execute the below command to be able
to edit the referred file with the [nano text editor](https://www.nano-editor.org/).

```shell
sudo nano {SECOND_LEVEL_DOMAIN_SLD}-le-ssl.conf
```

Ensure that the `RequestHeader` directives matches the content of the following snippet:

```text
RequestHeader set X-Forwarded-Proto "https"
RequestHeader set X-Forwarded-Port "443"
```

Check if it's necessary any further modifications, implement it if required and when finished, save
the file with the command `CTRL + O` and then exit the text editor  with the command `CTRL + X`.

Validate the Apache Server configuration and, if everything is correct, restart the
[Apache Server](https://httpd.apache.org/) to apply the updated configuration, executing the below
commands:

```shell
sudo apachectl configtest
sudo systemctl restart apache2
```

[SSL Labs Server Test](https://www.ssllabs.com/ssltest/) can be used to verify the certificate’s
grade and obtain detailed information about it, from the perspective of an external service.

To test if the [Certbot](https://certbot.eff.org/instructions?ws=apache&os=ubuntufocal) renewal
script includes the new domain (or subdomain), execute the following command:

```shell
sudo certbot renew --dry-run
```
