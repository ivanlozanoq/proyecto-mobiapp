# Proyecto MobiApp - pedido-app

Este repositorio contiene:

- Backend de la API (`backend/`): servicio Spring Boot que expone la funcionalidad de pedidos.
- Chart de Helm (`charts/pedido-app/`): manifiestos declarativos para desplegar la aplicación (y PostgreSQL opcional) en Kubernetes.
- Jenkins CI/CD (`jenkins/`): pipeline declarativo para construir la imagen Docker, publicar con tag SemVer y actualizar el chart para que ArgoCD sincronice (GitOps).
- Documentación adicional (`docs/`) y entornos (`environments/`).

## Flujo CI/CD (GitOps)

1. Se lanza el pipeline en Jenkins (`jenkins/Jenkinsfile`) con el parámetro `APP_VERSION` en formato SemVer (x.y.z). No se permite `latest`.
2. El pipeline:
   - Valida y normaliza `APP_VERSION`.
   - Construye y publica la imagen usando Kaniko en Kubernetes.
   - Actualiza `charts/pedido-app/values.yaml` (campo `image.tag`).
   - Hace commit y push a `main`.
3. ArgoCD (con Auto-Sync, Prune y Self-Heal) sincroniza los cambios desde `main` y aplica el despliegue.

## Estructura

- `backend/`: código Java Spring Boot.
- `charts/pedido-app/`: chart Helm de la aplicación.
  - `values.yaml`: 
    - `image.repository` y `image.tag`: imagen del backend. 
    - `replicaCount` y `hpa`: réplicas y autoescalado.
    - `resources`: requests/limits del backend.
    - `postgresql.*`: configuración de la base de datos Bitnami (si `db.enabled=true`).
- `jenkins/`:
  - `Jenkinsfile`: pipeline principal (agente en K8s, Kaniko + Git). Comentado por bloques.
  - `values.yaml`: valores de Helm para el controller de Jenkins (recursos, nodeSelector, JENKINS_URL, etc.).

## Despliegue

- Requisitos del clúster: 
  - Nginx Ingress Controller (si usas `ingress.enabled`)
  - `do-block-storage` como `storageClass` para PVCs
  - (Opcional) `metrics-server` para que `kubectl top` y el HPA funcionen

- ArgoCD:
  - App apunta a `repo: proyecto-mobiapp`, `path: charts/pedido-app`, `targetRevision: main`.
  - Auto-Sync con Prune y Self-Heal activados.

## Jenkins

- Acceso local (port-forward):
  - `kubectl -n jenkins port-forward svc/jenkins 8083:8080` → `http://localhost:8083`
- Lanzar pipeline: `pedido-app-pipeline` → `Build with Parameters` → `APP_VERSION = x.y.z`.
- Política de tagging: solo SemVer (no `latest`).

## Buenas prácticas y notas

- Mantener siempre `image.tag` con un SemVer NUEVO por cada build (evita cachés y asegura reconciliación de ArgoCD).
- Requests/limits ajustados para dejar >15% de holgura en el nodo.
- El controller Jenkins está fijado al node-pool nuevo; los agentes también prefieren ese pool.
- Si el backend depende de la BD al arrancar, puede haber reinicios breves hasta que PostgreSQL esté listo (aceptable para el curso). Para endurecer: añadir `readinessProbe`/`livenessProbe` y/o `initContainer` de espera de BD.

## Comandos útiles

- Ver imagen desplegada:
  - `kubectl -n pedido-app describe deploy pedido-app-pedido-app | grep -n "Image:"`
- Ver recursos de pods/nodos (si hay metrics-server):
  - `kubectl top pods -A`
  - `kubectl top nodes`


