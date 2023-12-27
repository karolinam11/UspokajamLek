# Use a specific Node.js version (e.g., 14) that is recommended for Angular
FROM node:19-slim AS build

# Set the working directory to /app
WORKDIR /app

# Copy the package.json and package-lock.json to the working directory
COPY Uspokajam-lek-frontend/package.json .
COPY Uspokajam-lek-frontend/package-lock.json .

# Install Angular CLI globally
RUN npm install -g @angular/cli

# Copy the entire Angular project to the working directory
COPY Uspokajam-lek-frontend/ .

# Install dependencies
RUN npm install

# Expose the default port for Angular development server
EXPOSE 4200

# Start the Angular development server
CMD ["ng", "serve", "--host", "0.0.0.0"]
