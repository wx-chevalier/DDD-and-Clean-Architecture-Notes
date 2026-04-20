# Clean Architecture Example with Java and Spring Boot

## Summary

This context provides an example of Clean Architecture implementation using Java and Spring Boot, emphasizing the separation of concerns and the isolation of core logic from outside concerns.

## Abstract

The context discusses the Clean Architecture pattern, which allows for the isolation of core logic in an application, making it easier to change data source details without significant impact or major code rewrites. The pattern is composed of several components, including the "core" (Model Entities and Use Cases) and the "infrastructure" (Delivery, Gateways, Repositories, Configuration, and Main). The "core" contains the domain knowledge and the "feature" to be implemented, while the "infrastructure" supports the "core" in a real environment. The process flow involves interactions between these components to provide the desired behavior. The context also provides a Java implementation example of the Clean Architecture pattern.

Opinions

- The Clean Architecture pattern is beneficial for creating a production-ready application.
- The pattern allows for the isolation of core logic, making it easier to change data source details without significant impact or major code rewrites.
- The pattern enables a clear testing strategy, as the majority of tests can verify business logic without relying on protocols that can easily change.
- The pattern is composed of several components, including the "core" and the "infrastructure," which interact to provide the desired behavior.
- The context provides a pragmatic approach to understanding the main (and most complicated) topic in clean architecture.
- The context provides a Java implementation example of the Clean Architecture pattern.
- The context emphasizes the importance of separating concerns and isolating core logic from outside concerns.

# Clean Architecture Example with Java and Spring Boot

![img](https://cdn-images-1.readmedium.com/v2/resize:fit:800/0*spGqBTyrmsj4ZMii.jpeg)

Creating an application is easy, just sit to code and … _habemus application_. If you are stuck, just go to the Internet, search, and you are going to find tons of samples, frameworks, templates, that will help you with your application.But creating a Great, Production-Ready Application is a whole different story.

> The idea of Clean Architecture is to put delivery and gateway at the edges of our design. Business logic should not depend on whether we expose a REST or a GraphQL API, and it should not depend on where we get data from — a database, a microservice API exposed via gRPC or REST, or just a simple CSV file.

The pattern allows us to isolate the core logic of our application from outside concerns. Having our core logic isolated means we can easily change data source details **without a significant impact or major code rewrites to the codebase**.

One of the main advantages we also saw in having an app with clear boundaries is our testing strategy — the majority of our tests can verify our business logic **without relying on protocols that can easily change**.

## Components

I wanted to give a more pragmatic/simplistic approach that can help in the first incursion to the clean architecture. That’s why I’ll be omitting concepts that may feel unavoidable to architecture purists.

My only goal here is that you understand what I consider the main (and most complicated) topic in clean architecture:

# Component Model

![img](https://cdn-images-1.readmedium.com/v2/resize:fit:800/1*9dZAiG-os39HSXy6X9eI7Q.png)

# Core

The “**core”** will be described in the Model Entities and Use Case component. Here is the domain knowledge and the “feature” we want to implement.

## Model Entities

> _Entities encapsulate Enterprise wide business rules._

An entity can be an object with methods, or it can be a set of data structures and functions. It doesn’t matter as long as the entities can be used by many different applications in the enterprise. They represents the bare domain.

## Use cases

> which contain application-specific business rules. These would be counterparts to _Application Services_ with the caveat that each class should focus on one particular _Use Case_.

It encapsulates and implements all of the use cases of the system. These use cases orchestrate the flow of data to and from the entities, and direct those entities to use their enterprise wide business rules to achieve the goals of the use case.

# Infrastructure

All this is just because computer programming is not perfect, we need a lot of support to let the **\*core\*** work in a real environment. These components are known as the “**\*Infrastructure\***” is composed by :

## Delivery

> _In the Delivery we found the Interface Adapters that receives requests from the outside of the microservice and delivers a response to them._

It is common to implement it as a [REST](https://en.wikipedia.org/wiki/Representational_state_transfer) HTTP Server, or consume Message from some Message Broker, or Job scheduler

## Gateways

> _In the gateway we found the Interface Adapters that receives requests from the outside of the microservice and delivers (that’s where the name comes from) a response to them._

It is common to implement them as a REST HTTP Clients, Message Broker Producer, or any other API Client.

## Repositories

> _These are Interface Adapters to systems meant to store and retrieve (serialized) application objects (usually entity)._

Repository are the interfaces to getting entities as well as creating and changing them. They keep a list of methods that are used to communicate with data sources and return a single entity or a list of entities. (e.g. UserRepository)

## Configuration

> _The Configuration is the part of the system that composes the different components into a running system._

In this module we create the application according to the implementations that we are developing and the behavior that we want to give to it.

## Main

> The main is just the entry point to the application.

# Process flow

To provide the desired behaviour, these components interact between them. The flow of actions, usually follows this pattern:

![img](https://cdn-images-1.readmedium.com/v2/resize:fit:800/1*KHsuic0ZV5ksT5CWvAjU5A.png)

**Request Process Flow**

1. An external system performs a request (An HTTP request, a JMS message is available, etc.)
2. The Delivery creates various Model Entities from the request data
3. The Delivery calls an Use Case execute
4. The Use Case operates on Model Entities
5. The Use Case makes a request to read / write on the Repository
6. The Repository consumes some Model Entity from the Use Case request
7. The Repository interacts with the External Persistence (DB,Cache,etc)
8. The Repository creates Model Entities from the persisted data
9. The Use Case requests collaboration from a Gateway
10. The Gateway consumes the Model Entities provided by the Use Case request
11. The Gateway interacts with External Services (other API, microservices, put messages in queues, etc.)

# Java Implementation

# Model entities

The domain objects (e.g., a Movie or a Shooting Location) — they have no knowledge of where they’re stored.

<iframe class="gist-iframe" src="https://readmedium.com/gist/soyjuanmalopez/31edf40fd084529dbf63f79e0e50db13.js" allowfullscreen="" frameborder="0" height="undefined" width="undefined" style="box-sizing: border-box; border: 0px solid rgb(229, 231, 235); --tw-border-spacing-x: 0; --tw-border-spacing-y: 0; --tw-translate-x: 0; --tw-translate-y: 0; --tw-rotate: 0; --tw-skew-x: 0; --tw-skew-y: 0; --tw-scale-x: 1; --tw-scale-y: 1; --tw-pan-x: ; --tw-pan-y: ; --tw-pinch-zoom: ; --tw-scroll-snap-strictness: proximity; --tw-gradient-from-position: ; --tw-gradient-via-position: ; --tw-gradient-to-position: ; --tw-ordinal: ; --tw-slashed-zero: ; --tw-numeric-figure: ; --tw-numeric-spacing: ; --tw-numeric-fraction: ; --tw-ring-inset: ; --tw-ring-offset-width: 0px; --tw-ring-offset-color: #fff; --tw-ring-color: rgba(59,130,246,.5); --tw-ring-offset-shadow: 0 0 #0000; --tw-ring-shadow: 0 0 #0000; --tw-shadow: 0 0 #0000; --tw-shadow-colored: 0 0 #0000; --tw-blur: ; --tw-brightness: ; --tw-contrast: ; --tw-grayscale: ; --tw-hue-rotate: ; --tw-invert: ; --tw-saturate: ; --tw-sepia: ; --tw-drop-shadow: ; --tw-backdrop-blur: ; --tw-backdrop-brightness: ; --tw-backdrop-contrast: ; --tw-backdrop-grayscale: ; --tw-backdrop-hue-rotate: ; --tw-backdrop-invert: ; --tw-backdrop-opacity: ; --tw-backdrop-saturate: ; --tw-backdrop-sepia: ; display: block; vertical-align: middle; max-width: 100%; width: 680px; min-height: 240px; height: 346px;"></iframe>

## Use Case

are classes that orchestrate and perform domain actions — think of Service Objects or Use Case Objects. They implement complex business rules and validation logic specific to a domain action (e.g., onboarding a production)

Example the use case:

<iframe class="gist-iframe" src="https://readmedium.com/gist/soyjuanmalopez/c59f0dd00dfd8c2ebee02d017f9bf359.js" allowfullscreen="" frameborder="0" height="undefined" width="undefined" style="box-sizing: border-box; border: 0px solid rgb(229, 231, 235); --tw-border-spacing-x: 0; --tw-border-spacing-y: 0; --tw-translate-x: 0; --tw-translate-y: 0; --tw-rotate: 0; --tw-skew-x: 0; --tw-skew-y: 0; --tw-scale-x: 1; --tw-scale-y: 1; --tw-pan-x: ; --tw-pan-y: ; --tw-pinch-zoom: ; --tw-scroll-snap-strictness: proximity; --tw-gradient-from-position: ; --tw-gradient-via-position: ; --tw-gradient-to-position: ; --tw-ordinal: ; --tw-slashed-zero: ; --tw-numeric-figure: ; --tw-numeric-spacing: ; --tw-numeric-fraction: ; --tw-ring-inset: ; --tw-ring-offset-width: 0px; --tw-ring-offset-color: #fff; --tw-ring-color: rgba(59,130,246,.5); --tw-ring-offset-shadow: 0 0 #0000; --tw-ring-shadow: 0 0 #0000; --tw-shadow: 0 0 #0000; --tw-shadow-colored: 0 0 #0000; --tw-blur: ; --tw-brightness: ; --tw-contrast: ; --tw-grayscale: ; --tw-hue-rotate: ; --tw-invert: ; --tw-saturate: ; --tw-sepia: ; --tw-drop-shadow: ; --tw-backdrop-blur: ; --tw-backdrop-brightness: ; --tw-backdrop-contrast: ; --tw-backdrop-grayscale: ; --tw-backdrop-hue-rotate: ; --tw-backdrop-invert: ; --tw-backdrop-opacity: ; --tw-backdrop-saturate: ; --tw-backdrop-sepia: ; display: block; vertical-align: middle; max-width: 100%; width: 680px; min-height: 240px; height: 302px;"></iframe>

By having business logic extracted into use case, we are not coupled to a particular transport layer or controller implementation. The use case can be triggered not only by a controller, but also by an event, a cron job, or from the command line.

## Delivery

Transport Layer can trigger an interactor to perform business logic. We treat it as an input for our system. The most common transport layer for microservices is the HTTP API Layer and a set of controllers that handle requests.

<iframe class="gist-iframe" src="https://readmedium.com/gist/soyjuanmalopez/b6d5e352487eaaec537a5fd185c38f65.js" allowfullscreen="" frameborder="0" height="undefined" width="undefined" style="box-sizing: border-box; border: 0px solid rgb(229, 231, 235); --tw-border-spacing-x: 0; --tw-border-spacing-y: 0; --tw-translate-x: 0; --tw-translate-y: 0; --tw-rotate: 0; --tw-skew-x: 0; --tw-skew-y: 0; --tw-scale-x: 1; --tw-scale-y: 1; --tw-pan-x: ; --tw-pan-y: ; --tw-pinch-zoom: ; --tw-scroll-snap-strictness: proximity; --tw-gradient-from-position: ; --tw-gradient-via-position: ; --tw-gradient-to-position: ; --tw-ordinal: ; --tw-slashed-zero: ; --tw-numeric-figure: ; --tw-numeric-spacing: ; --tw-numeric-fraction: ; --tw-ring-inset: ; --tw-ring-offset-width: 0px; --tw-ring-offset-color: #fff; --tw-ring-color: rgba(59,130,246,.5); --tw-ring-offset-shadow: 0 0 #0000; --tw-ring-shadow: 0 0 #0000; --tw-shadow: 0 0 #0000; --tw-shadow-colored: 0 0 #0000; --tw-blur: ; --tw-brightness: ; --tw-contrast: ; --tw-grayscale: ; --tw-hue-rotate: ; --tw-invert: ; --tw-saturate: ; --tw-sepia: ; --tw-drop-shadow: ; --tw-backdrop-blur: ; --tw-backdrop-brightness: ; --tw-backdrop-contrast: ; --tw-backdrop-grayscale: ; --tw-backdrop-hue-rotate: ; --tw-backdrop-invert: ; --tw-backdrop-opacity: ; --tw-backdrop-saturate: ; --tw-backdrop-sepia: ; display: block; vertical-align: middle; max-width: 100%; width: 680px; min-height: 240px; height: 588px;"></iframe>

Each Handler handles a specific requests, converts the request payload to DTO or model entities, and calls the appropriate execute. Finally, it converts the execute response into the response payload and sends it back it to the caller.

## Gateways

Here the Gateway takes advantage of a provided RestClient to connect to an external service.

<iframe class="gist-iframe" src="https://readmedium.com/gist/soyjuanmalopez/d21038320e6cd11f30cfcb3ea96aaaf5.js" allowfullscreen="" frameborder="0" height="undefined" width="undefined" style="box-sizing: border-box; border: 0px solid rgb(229, 231, 235); --tw-border-spacing-x: 0; --tw-border-spacing-y: 0; --tw-translate-x: 0; --tw-translate-y: 0; --tw-rotate: 0; --tw-skew-x: 0; --tw-skew-y: 0; --tw-scale-x: 1; --tw-scale-y: 1; --tw-pan-x: ; --tw-pan-y: ; --tw-pinch-zoom: ; --tw-scroll-snap-strictness: proximity; --tw-gradient-from-position: ; --tw-gradient-via-position: ; --tw-gradient-to-position: ; --tw-ordinal: ; --tw-slashed-zero: ; --tw-numeric-figure: ; --tw-numeric-spacing: ; --tw-numeric-fraction: ; --tw-ring-inset: ; --tw-ring-offset-width: 0px; --tw-ring-offset-color: #fff; --tw-ring-color: rgba(59,130,246,.5); --tw-ring-offset-shadow: 0 0 #0000; --tw-ring-shadow: 0 0 #0000; --tw-shadow: 0 0 #0000; --tw-shadow-colored: 0 0 #0000; --tw-blur: ; --tw-brightness: ; --tw-contrast: ; --tw-grayscale: ; --tw-hue-rotate: ; --tw-invert: ; --tw-saturate: ; --tw-sepia: ; --tw-drop-shadow: ; --tw-backdrop-blur: ; --tw-backdrop-brightness: ; --tw-backdrop-contrast: ; --tw-backdrop-grayscale: ; --tw-backdrop-hue-rotate: ; --tw-backdrop-invert: ; --tw-backdrop-opacity: ; --tw-backdrop-saturate: ; --tw-backdrop-sepia: ; display: block; vertical-align: middle; max-width: 100%; width: 680px; min-height: 240px; height: 240px;"></iframe>

The Gateway has no Business logic, but conversions and mappings.

## Repositories

A data source implements methods defined on the repository and stores the implementation of fetching and pushing the data.

<iframe class="gist-iframe" src="https://readmedium.com/gist/soyjuanmalopez/b6d5e352487eaaec537a5fd185c38f65.js" allowfullscreen="" frameborder="0" height="undefined" width="undefined" style="box-sizing: border-box; border: 0px solid rgb(229, 231, 235); --tw-border-spacing-x: 0; --tw-border-spacing-y: 0; --tw-translate-x: 0; --tw-translate-y: 0; --tw-rotate: 0; --tw-skew-x: 0; --tw-skew-y: 0; --tw-scale-x: 1; --tw-scale-y: 1; --tw-pan-x: ; --tw-pan-y: ; --tw-pinch-zoom: ; --tw-scroll-snap-strictness: proximity; --tw-gradient-from-position: ; --tw-gradient-via-position: ; --tw-gradient-to-position: ; --tw-ordinal: ; --tw-slashed-zero: ; --tw-numeric-figure: ; --tw-numeric-spacing: ; --tw-numeric-fraction: ; --tw-ring-inset: ; --tw-ring-offset-width: 0px; --tw-ring-offset-color: #fff; --tw-ring-color: rgba(59,130,246,.5); --tw-ring-offset-shadow: 0 0 #0000; --tw-ring-shadow: 0 0 #0000; --tw-shadow: 0 0 #0000; --tw-shadow-colored: 0 0 #0000; --tw-blur: ; --tw-brightness: ; --tw-contrast: ; --tw-grayscale: ; --tw-hue-rotate: ; --tw-invert: ; --tw-saturate: ; --tw-sepia: ; --tw-drop-shadow: ; --tw-backdrop-blur: ; --tw-backdrop-brightness: ; --tw-backdrop-contrast: ; --tw-backdrop-grayscale: ; --tw-backdrop-hue-rotate: ; --tw-backdrop-invert: ; --tw-backdrop-opacity: ; --tw-backdrop-saturate: ; --tw-backdrop-sepia: ; display: block; vertical-align: middle; max-width: 100%; width: 680px; min-height: 240px;"></iframe>

## Configuration

This example of code, there the configuration of the behavior of all our layers. So we send a bean to all those beans that depend on it.

<iframe class="gist-iframe" src="https://readmedium.com/gist/soyjuanmalopez/90ceeef4e53837b1a12690e297cde2c8.js" allowfullscreen="" frameborder="0" height="undefined" width="undefined" style="box-sizing: border-box; border: 0px solid rgb(229, 231, 235); --tw-border-spacing-x: 0; --tw-border-spacing-y: 0; --tw-translate-x: 0; --tw-translate-y: 0; --tw-rotate: 0; --tw-skew-x: 0; --tw-skew-y: 0; --tw-scale-x: 1; --tw-scale-y: 1; --tw-pan-x: ; --tw-pan-y: ; --tw-pinch-zoom: ; --tw-scroll-snap-strictness: proximity; --tw-gradient-from-position: ; --tw-gradient-via-position: ; --tw-gradient-to-position: ; --tw-ordinal: ; --tw-slashed-zero: ; --tw-numeric-figure: ; --tw-numeric-spacing: ; --tw-numeric-fraction: ; --tw-ring-inset: ; --tw-ring-offset-width: 0px; --tw-ring-offset-color: #fff; --tw-ring-color: rgba(59,130,246,.5); --tw-ring-offset-shadow: 0 0 #0000; --tw-ring-shadow: 0 0 #0000; --tw-shadow: 0 0 #0000; --tw-shadow-colored: 0 0 #0000; --tw-blur: ; --tw-brightness: ; --tw-contrast: ; --tw-grayscale: ; --tw-hue-rotate: ; --tw-invert: ; --tw-saturate: ; --tw-sepia: ; --tw-drop-shadow: ; --tw-backdrop-blur: ; --tw-backdrop-brightness: ; --tw-backdrop-contrast: ; --tw-backdrop-grayscale: ; --tw-backdrop-hue-rotate: ; --tw-backdrop-invert: ; --tw-backdrop-opacity: ; --tw-backdrop-saturate: ; --tw-backdrop-sepia: ; display: block; vertical-align: middle; max-width: 100%; width: 680px; min-height: 240px; height: 720px;"></iframe>

## Main

Finally, the main class ends being a very boring one:

<iframe class="gist-iframe" src="https://readmedium.com/gist/soyjuanmalopez/ecfa9acfd66d8e38bba371940f4d4232.js" allowfullscreen="" frameborder="0" height="undefined" width="undefined" style="box-sizing: border-box; border: 0px solid rgb(229, 231, 235); --tw-border-spacing-x: 0; --tw-border-spacing-y: 0; --tw-translate-x: 0; --tw-translate-y: 0; --tw-rotate: 0; --tw-skew-x: 0; --tw-skew-y: 0; --tw-scale-x: 1; --tw-scale-y: 1; --tw-pan-x: ; --tw-pan-y: ; --tw-pinch-zoom: ; --tw-scroll-snap-strictness: proximity; --tw-gradient-from-position: ; --tw-gradient-via-position: ; --tw-gradient-to-position: ; --tw-ordinal: ; --tw-slashed-zero: ; --tw-numeric-figure: ; --tw-numeric-spacing: ; --tw-numeric-fraction: ; --tw-ring-inset: ; --tw-ring-offset-width: 0px; --tw-ring-offset-color: #fff; --tw-ring-color: rgba(59,130,246,.5); --tw-ring-offset-shadow: 0 0 #0000; --tw-ring-shadow: 0 0 #0000; --tw-shadow: 0 0 #0000; --tw-shadow-colored: 0 0 #0000; --tw-blur: ; --tw-brightness: ; --tw-contrast: ; --tw-grayscale: ; --tw-hue-rotate: ; --tw-invert: ; --tw-saturate: ; --tw-sepia: ; --tw-drop-shadow: ; --tw-backdrop-blur: ; --tw-backdrop-brightness: ; --tw-backdrop-contrast: ; --tw-backdrop-grayscale: ; --tw-backdrop-hue-rotate: ; --tw-backdrop-invert: ; --tw-backdrop-opacity: ; --tw-backdrop-saturate: ; --tw-backdrop-sepia: ; display: block; vertical-align: middle; max-width: 100%; width: 680px; min-height: 240px; height: 240px;"></iframe>

# Package structure

This pragmatic architecture is separated in two sub-projects, core and infrastructure:

![img](https://cdn-images-1.readmedium.com/v2/resize:fit:800/0*wVstoFXz9-iVLnpA.png)

## Core

Domain and Application layers both represent core, however, their nature is of 2 kinds:

**Domain business logic**: here you find the “models” of your app, which can be of different types (Aggregate Roots, Entities, Value Objects) and that implement enterprise-wide business rules (they not only contain data but also processes).

**Application business logic**: here you find the so-called _“usecases”,_ situated on top of models and the _“ports”_ for the Data Layer (used for dependency inversion, usually **Repository interfaces**), they retrieve and store domain models by using either repositories or other Use Cases.

![img](https://cdn-images-1.readmedium.com/v2/resize:fit:800/0*kbZEK-moceXI-5em.png)

# Infrastructure

Is necessary lot of support to let the core work in a real environment. These components are known as the “**\*Infrastructure\***”.

**Persistence** might be an adapter to a SQL database (an Active Record class in Rails or JPA in Java), an elastic search adapter, REST API, or even an adapter to something simple such as a CSV file or a Hash. A data source implements methods defined on the repository and stores the implementation of fetching and pushing the data.

**Delivery** can trigger an interactor to perform business logic. We treat it as an input for our system. The most common transport layer for microservices is the HTTP and a set of controllers that handle requests. By having business logic extracted into use case, we are not coupled to a particular transport layer or controller implementation. Use case can be triggered not only by a controller, but also by an event, a cron job, or from the command line.

## Repository Example

## soyjuanmalopez/clean-architecture

### A example of clean architecture in Java 8 and Spring Boot 2.0 - soyjuanmalopez/clean-architecture

github.com

# Conclusion

We are in a great position when it comes to swapping data sources to different microservices. One of the key benefits is that we can delay some of the decisions about whether and how we want to store data internal to our application. Based on the feature’s use case, we even have the flexibility to determine the type of data store — whether it be Relational or Documents. At the beginning of a project, we have the least amount of information about the system we are building. We should not lock ourselves into an architecture with uninformed decisions leading to a project paradox. The decisions we made make sense for our needs now and have enabled us to move fast. The best part of clean architecture is that it keeps our application flexible for future requirements to come.
