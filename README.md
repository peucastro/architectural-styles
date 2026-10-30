# Architectural Styles

The same small application (users, items, orders) built three times: Hexagonal, Onion and Clean. Each project is an independent Maven project with Java 21 and no frameworks. Only the JDK HTTP server is used, and storage is in-memory.

## Run

```bash
cd hexagonal && mvn compile && java -cp target/classes com.example.hexagonal.Main   # :8080
cd onion     && mvn compile && java -cp target/classes com.example.onion.Main       # :8081
cd clean     && mvn compile && java -cp target/classes com.example.clean.frameworks.Main # :8082
```

```bash
curl -X POST localhost:8080/users  -d "name=Ana&email=ana@x.com"
curl -X POST localhost:8080/items  -d "name=Book&price=10.50"
curl -X POST localhost:8080/orders -d "userId=<userId>&itemIds=<itemId1>,<itemId2>"
```

## The three shapes

Dependencies point inward in all three. The domain never imports anything from outside.

**Hexagonal** (ports and adapters)

```
adapter.in.http  ──calls──▶  port.in (interfaces)  ◀── implemented by ── application.service
                                                                              │ uses
adapter.out.memory ── implements ──▶  port.out (interfaces)  ◀───────────────┘
                                                  
domain  (User, Item, Order)  ◀── used by application.service
```

**Onion** (layers, innermost first)

```
domain.model        User, Item, Order
domain.service      PricingService (logic spanning several items)
application.repository   repository interfaces   (defined in the core)
application.service      UserService, ItemService, OrderService (use repositories and domain)
infrastructure.persistence  in-memory repos (implement application.repository)
infrastructure.web          WebController (calls application services directly)
```

**Clean** (Robert C. Martin's rings)

```
entities                       User, Item, Order
usecases.<name>                InputBoundary → Interactor → OutputBoundary
usecases.gateways              gateway interfaces, owned by the use case layer
interfaceadapters.controllers  HttpController  (HTTP → input boundary)
interfaceadapters.presenters   HttpPresenter   (output boundary → view model)
interfaceadapters.gateways     in-memory gateways
frameworks.Main                wiring and HTTP server
```

## What differs

| Question | Hexagonal | Onion | Clean |
|---|---|---|---|
| Where do the use case interfaces live? | `port.in` (explicit inbound port per use case) | None: the application services are concrete classes | Input boundary per use case |
| Where do the repository interfaces live? | `port.out` | `application.repository` | `usecases.gateways` |
| Who formats the output? | The HTTP adapter | The web controller | A presenter behind an output boundary |
| Domain service? | Not used | `PricingService` (total of items) | Not used |

The business rules are nearly identical in all three. The differences are where the interfaces are declared and how many objects a request passes through.
