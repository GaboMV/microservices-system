# Sistema de Microservicios - Ventas

Este proyecto implementa un sistema de ventas distribuido utilizando **Spring Boot**, con microservicios para gestión de productos, ventas, contabilidad, orquestación y un API Gateway. Todos los microservicios se comunican mediante REST y se registran en un servicio de descubrimiento (Eureka).

---

## Estructura de los Microservicios

| Servicio                | Función principal                                               |
|-------------------------|-----------------------------------------------------------------|
| `warehouse-service`     | Gestión de productos y stock                                    |
| `accounting-service`    | Registro de asientos contables                                  |
| `sale-service`          | Gestión de ventas                                               |
| `gateway-service`       | API Gateway                                                     |
| `orchestrator-service`  | Orquestador de procesos de venta                                 |
| `discovery-service`     | Servicio de descubrimiento (Eureka)                             |

---

## Configuración de Base de Datos

La configuración de cada microservicio utiliza PostgreSQL y Mysql:

jdbc:postgresql://localhost:15432/sales
jdbc:postgresql://localhost:15432/accounting
jdbc:mysql://localhost:13306/warehouse

## Ejecución de microservicios

Los mircoservicios a ejecutar son:

discovery-service

warehouse-service

accounting-service

sale-service

orchestrator-service

gateway-service

Para ejecutar cada microservicio:

cd <nombre-del-servicio>
mvn spring-boot:run -X

## Probar la Aplicación

Abrir el archivo test-sales.http incluido en la carpeta raiz proyecto.

Todas las solicitudes que pasan por el API Gateway usan:

http://localhost:8082

El Saga orchestrator usa: 

http://localhost:8082

Y el Discovery usa: 

http://localhost:8761


Para realizar un post en venta que use el servicio Saga, hay que usar el endpoint: 
http://localhost:8082/ms-orch/saga/sale 

Ejemplo de solicitud:
POST  http://localhost:8082/ms-orch/saga/sale 
Content-Type: application/json
Accept: application/json

{
  "saleNumber": "SALE-0025", #cambiar numero de venta en cada consulta
  "productId": 5,
  "quantity": 1,
  "debit_amount": 8.99, 
  "credit_amount": 10.00,
  "unitPrice": 1.99,  #UnitPrice en 0.99 para disparar el trigger
  "totalAmount": 0.99,
  "discountPercentage": 10.0,
  "discountAmount": 50.25,
  "finalAmount": 452.25,
  "saleDate": "2025-09-02",
  "customerId": 1,
  "customerName": "Juan Perez",
  "salesperson": "Maria Lopez",
  "paymentMethod": "card",
  "paymentStatus": "pending",
  "notes": "Venta prueba",
  "createdAt": "2025-09-02T10:00:00",
  "updatedAt": "2025-09-02T10:00:00"
}


## Notas importantes

Las ventas se registran en la base de datos sales.

Los microservicios actualizan stock y registran asientos contables automáticamente mediante llamadas REST.

El orquestador asegura que la venta, el stock y el asiento contable se procesen correctamente.

El Gateway centraliza todas las solicitudes externas y redirige a los microservicios correspondientes.

Se utiliza Eureka para el descubrimiento automático de servicios.
