# Spring CRUD Generator Demo

This repository is a **demo project** for **Spring CRUD Generator**:

- Generator repository: https://github.com/mzivkovicdev/spring-crud-generator

The purpose of this demo is to provide a **real, buildable example** that demonstrates the generator in action and helps users evaluate:
- the **stability** of the generator (repeatable output, consistent structure),
- the **quality and style** of the generated code (layers, conventions, DTOs, validations, etc.),
- what a project looks like after generation.

---

## CRUD specification used in this demo

This demo contains a `crud-spec.yaml` file that is based on the full example specification from the generator repository:

- Source spec (full example):  
  https://github.com/mzivkovicdev/spring-crud-generator/blob/master/docs/examples/crud-spec-full.yaml

The goal is to keep the demo spec aligned with a real-world usage scenario so you can clearly see the kind of code Spring CRUD Generator produces.

---

## What to look at

If you want to quickly review what the generator produces, explore:
- package/module structure and layering (e.g. controller/service/repository),
- generated DTOs and validation,
- mappings,
- database-related artifacts (migrations),
- overall naming conventions and project layout.

This repo is intentionally meant to be a **showcase** of the generated output.

---

## Regenerating the project (optional)

If you want to reproduce the generation locally, use `crud-spec.yaml` from this repository and follow the usage instructions from the main project:

- Documentation / usage: https://github.com/mzivkovicdev/spring-crud-generator

---

## Why this repository exists

Generators are easiest to trust when you can inspect a complete, working example.  
This demo project exists to provide:
- a **stable reference output**,
- a quick way to inspect generated code without setting anything up from scratch,
- an end-to-end example that can be built and run like a normal Spring project.

---

## License

This demo project follows the [LICENSE](./LICENSE) defined for the repository.
