# websocket
## Aplicação em kotlin lendo informação de uma fila SQS e informando o front-end em angular, utilizando spring websocket

### Objetivo

O objetivo de projeto e mostrar com o backend pode enviar informações diretamente para o frontend através de websocket.
Com esse projeto uma informação é enviada para uma fila SQS, processada pelo backend e enviada diretamente para o frontend, sem que o frontend precise fazer uma requisição http para o backend.

### Informações do projeto

- Para o backend foi utilizado kotlin com JVM 17, Spring boot 2.7.3 e Spring cloud 2.3.0
- No frontend foi utlizando angular na versão 14.0.2 e primNG.
- Para simular uma fila SQS foi utilizado localstack.

### Configurando localstack

Para utilizar o localstack, vamos utlizar a versão docker:

crie o arquivo ```docker-compose.yml```
dentro dele coloque o seguinte código:

```
version: '3.3'

services: 

  localstack:
    image: localstack/localstack:latest ## imagem do localstack
    container_name: "localstack" ## nome do container
    environment: 
      - AWS_ACCESS_KEY_ID=MYAWSACCESSKEYID  ## informações referente a chaves aws (pode criar qualquer informação aqui)
      - AWS_SECRET_ACCESS_KEY=MYAWSSECRETACCESSKEY ## informações referente a chaves aws (pode criar qualquer informação aqui)
      - AWS_DEFAULT_REGION=us-east-1 ## Região aws
      - AWS_DEFAULT_OUTPUT=json ## formato do dado
      - EDGE_PORT=4566 ## porta de acesso
      - SERVICES=sqs ## serviço aws que será simulado
    ports: 
      - '4566-4583:4566-4583' ## portas dos serviços
```

Em seguida execute o comando: ```docker-compose up -d```
Para que funcione de uma forma mais fácil, instale o aws cli:
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```
Depois configure o aws cli para conversar com o localstack ```aws config``` e coloque as mesmas informações que foram adicionadas no arquivo ```docker-compose.yml```

Após tudo pronto, cria fila *test-websocket*, para isso, acesse a instancia do localstack ```docker exec -it localstack sh``` e execute o comando ```awslocal sqs create-queue --queue-name test-websocket```

### Compilando o projeto backend

Entre na pasta *webSocketApp* e execute o comando ```./gradlew clean build```, quando terminar basta executar o comando ```java -jar build/libs/webSocketApp-0.0.1-SNAPSHOT.jar```. Pronto a aplicação está no ar.

### Compilando o projeto frontend
#### IMPORTANTE PARA QUE FUNCIONE O ANGULAR 14 DEVE ESTAR INSTALADO.

Entre na pasta *angular-primeng* e execute o comando ```npm install```, logo em seguida execute o comando ```ng build``` e ai execute a aplicação com o comando ```ng serve```. Para acessar a aplicação, basta ir no browser e digitar (http://localhost:4200/)

### Enviando informações para a fila.

Para isso, basta importar no [Postman](https://www.postman.com/) o conteudo da pasta *postman*

