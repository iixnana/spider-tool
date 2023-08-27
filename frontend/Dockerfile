FROM node:17-alpine3.12
WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY package.json .
COPY package-lock.json .
RUN npm install # -g npm@9.8.1
COPY . .
EXPOSE 3000
CMD ["npm", "start"]
