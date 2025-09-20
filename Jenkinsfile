pipeline {
  agent any

  parameters {
    string(name: 'APP_VERSION', defaultValue: '0.1.4', description: 'Nueva versión SemVer (x.y.z), NO usar latest')
  }

  environment {
    IMAGE_REPO = 'ivan150/pedidos-api'
    CHART_VALUES = 'charts/pedido-app/values.yaml'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
        sh 'git --version'
      }
    }

    stage('Validar versión') {
      steps {
        script {
          if (!(params.APP_VERSION ==~ /^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)$/)) {
            error "APP_VERSION debe ser SemVer x.y.z (ej. 0.1.4). Valor recibido: ${params.APP_VERSION}"
          }
          if (params.APP_VERSION == 'latest') {
            error 'No se permite usar la etiqueta mutable latest'
          }
        }
      }
    }

    stage('Docker login') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh '''
            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
          '''
        }
      }
    }

    stage('Buildx setup') {
      steps {
        sh '''
          docker buildx version || true
          # Crear e inicializar builder si no existe
          docker buildx create --name ci-builder --use || docker buildx use ci-builder
          docker buildx inspect --bootstrap
        '''
      }
    }

    stage('Build & Push multi-arch') {
      steps {
        sh '''
          docker buildx build \
            --platform linux/amd64,linux/arm64 \
            -f backend/Dockerfile \
            -t ${IMAGE_REPO}:${APP_VERSION} \
            --push .
        '''
      }
    }

    stage('Bump image.tag en Helm values') {
      steps {
        sh '''
          echo "Actualizando ${CHART_VALUES} a tag ${APP_VERSION}"
          # Reemplazo seguro de la línea tag: "..."
          sed -E -i 's|(tag:\s*\")([^"]+)(\")|\\1'"${APP_VERSION}"'\\3|' ${CHART_VALUES}
          echo 'Preview del cambio:'
          grep -n -E 'repository:|tag:' -n ${CHART_VALUES} | sed -n '1,10p'
        '''
      }
    }

    stage('Commit & Push') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'github-credentials', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
          sh '''
            git config user.name "jenkins-bot"
            git config user.email "jenkins-bot@local"
            git add ${CHART_VALUES}
            git commit -m "ci: bump image.tag to ${APP_VERSION}"
            # Forzar URL con credenciales
            git remote set-url origin https://${GIT_USER}:${GIT_PASS}@github.com/ivanlozanoq/proyecto-mobiapp.git
            git push origin HEAD:main
          '''
        }
      }
    }
  }
}
