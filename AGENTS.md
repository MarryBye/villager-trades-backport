# AGENTS.md — Villager Trades Backport Development Guide

Руководство для AI-агентов по структуре, механикам, правилам и рабочим процессам в проекте **Villager Trades Backport (Minecraft 1.7.10)**.

---

## 📌 О проекте

- **Название:** Villager Trades Backport
- **Платформа:** Minecraft 1.7.10 / Minecraft Forge `10.13.4.1614`
- **Шаблон и сборочный стек:** GTNH ExampleMod / RetroFuturaGradle (RFG) + UniMixins (Mixin 0.8.7)
- **Авторы:** `MarryBye + Gemini AI`
- **Цель:** Перенос и бэкпорт современной системы торговли деревенских жителей (Village & Pillage 1.14+ и Villager Trade Rebalance) в Minecraft 1.7.10 на основе открытого мода с глубокой адаптацией под стек GTNH.
- **Ключевые механики:**
  - Уровни опыта жителей (Novice, Apprentice, Journeyman, Expert, Master) со шкалой прогресса.
  - Пополнение запасов сделок (Restocking) у рабочих мест до 2 раз в день.
  - Динамическое ценообразование: скидки за популярность, скидки за спасение жителей-зомби, рост цен при чрезмерном спросе.
  - Современный двухстраничный интерфейс торговли со списком сделок слева и быстрым обменом в один клик.
  - Поддержка биомных костюмов и эксклюзивных сделок (Trade Rebalance).
  - Глубокая интеграция с [VillageNames](https://github.com/GTNewHorizons/VillageNames) и экосистемой GTNH.

---

## 📂 Архитектура и структура файлов

```text
villager-trades-backport/
├── .agents/
│   └── skills/
│       ├── villager-trades-backport-rules/
│       │   └── SKILL.md                          # Обязательные правила, Conventional Commits и SemVer
│       └── client-testing/
│           └── SKILL.md                          # Процесс тестирования и верификации клиента
├── libs/                                         # Локальные dev-зависимости мода (в .gitignore)
│   ├── +unimixins-all-1.7.10-*.jar               # Миксины для Forge 1.7.10 (UniMixins) [Обязательно]
│   ├── gtnhlib-*.jar                             # Базовые утилиты и сетевой слой GTNHLib [Обязательно]
│   ├── lwjgl3ify-*.jar                           # LWJGL 3 бэкенд, ввод и шрифты (lwjgl3ify) [Обязательно]
│   ├── VillageNames-*.jar                        # Профессии, имена и деревни (VillageNames) [Обязательно]
│   ├── angelica-*.jar                            # Графический движок Sodium / Iris (Angelica) [Поддерживается]
│   ├── etfuturum-*.jar                           # Бэкпорт предметов и блоков (Et Futurum Requiem) [Поддерживается]
│   ├── hodgepodge-*.jar                          # Патчи, фиксы и оптимизации (Hodgepodge) [Поддерживается]
│   ├── NEIIntegration-*.jar                      # Интеграция рецептов в NEI [Поддерживается]
│   ├── NotEnoughItems-*.jar                      # Рецепты и интерфейсы NEI [Поддерживается]
│   └── TConstruct-*.jar                          # Tinkers' Construct (профессии и торговля) [Поддерживается]
├── src/main/
│   ├── java/com/marrybye/villagertradesbackport/
│   │   ├── VillagerTradesBackport.java           # Главный класс мода (@Mod)
│   │   ├── CommonProxy.java                      # Инициализация общего прокси, событий и сети
│   │   ├── ClientProxy.java                      # Клиентская регистрация рендеров и GUI
│   │   ├── Config.java                           # Управление конфигурацией Forge
│   │   ├── api/                                  # API для сторонних модов и интеграций
│   │   ├── compat/                               # Модули совместимости (VillageNames, TiC, NEI, EFR)
│   │   ├── entity/                               # Логика уровней, опыта, спроса и данных жителей
│   │   ├── gui/                                  # Современный GUI торговли (список предложений, клики)
│   │   ├── mixins/                               # Миксины в ванильные классы EntityVillager, GuiMerchant и др.
│   │   └── network/                              # Сетевые пакеты синхронизации сделок и выбора предложений
│   └── resources/
│       ├── mcmod.info                            # Метаданные мода для FML
│       ├── mixins.villagertradesbackport.json    # Конфигурация UniMixins
│       └── assets/villagertradesbackport/
│           ├── lang/                             # Локализации (en_US, ru_RU)
│           └── textures/gui/                     # Текстуры современного торгового GUI и иконки уровней
├── build.gradle.kts                              # GTNH Convention Plugin
├── dependencies.gradle                           # Подключение зависимостей и libs/
├── gradle.properties                             # Настройки мода (modId, modName, modGroup, mixins)
└── README.md                                     # Документация мода для пользователей и разработчиков
```

---

## 🧩 Внешние зависимости и матрица совместимости (Compatibility Architecture)

| Зависимость | Статус | Требования к разработке и интеграция |
| :--- | :--- | :--- |
| [**UniMixins**](https://github.com/GTNewHorizons/UniMixins) | **Обязательная** | Обеспечивает интеграцию Mixin 0.8.7. Используется для модификации ванильного `EntityVillager`, перехвата открытия `GuiMerchant`, внедрения трекинга опыта, спроса и механики пополнения запасов. |
| [**GTNHLib**](https://github.com/GTNewHorizons/GTNHLib) | **Обязательная** | Предоставляет фундаментальные утилиты, хелперы сериализации, аннотации и стабильную работу на сборочном стеке GTNH. |
| [**lwjgl3ify**](https://github.com/GTNewHorizons/lwjgl3ify) | **Обязательная** | Современный бэкенд LWJGL 3, поддержка высокого DPI, сырой ввод мыши для плавного скроллинга и клика по торговым предложениям в GUI. |
| [**VillageNames**](https://github.com/GTNewHorizons/VillageNames) | **Обязательная** | Глубокая синергия: совместимость с кастомными профессиями жителей, их именами, структурой деревень и биомными вариациями из мода VillageNames. |
| [**Angelica**](https://github.com/GTNewHorizons/Angelica) | Поддерживается | Графический движок Sodium / Iris для 1.7.10. Обеспечивает плавный рендеринг торгового GUI и совместимость с шейдерами. Любые прямые вызовы классов Angelica изолируются проверкой `Loader.isModLoaded("angelica")`. |
| [**Et Futurum Requiem**](https://github.com/GTNewHorizons/Et-Futurum-Requiem) | Поддерживается | Интеграция современных предметов и блоков (фонари, бочки, колокола, новые зачарования и ресурсы) в таблицы торговли соответствующих профессий. |
| [**Hodgepodge**](https://github.com/GTNewHorizons/Hodgepodge) | Поддерживается | Платформенные фиксы и оптимизации ванильных сущностей и инвентарей. |
| [**Not Enough Items (NEI)**](https://github.com/GTNewHorizons/NotEnoughItems) | Поддерживается | Отображение интерфейса NEI поверх окна торговли, поддержка поиска и быстрых подсказок по предметам сделок. |
| [**NEI Integration**](https://github.com/GTNewHorizons/NEI-Integration) | Поддерживается | Поддержка отображения рецептов сделок жителей внутри каталога NEI. |
| [**Tinkers' Construct (TiC)**](https://github.com/GTNewHorizons/TinkersConstruct) | Поддерживается | Совместимость с кастомными жителями TiC (например, житель в деревне с плавильной станцией) без сбоев торговли. |

> [!IMPORTANT]
> **Принцип мягкой совместимости (Soft-Dependencies):**  
> Мод должен гарантированно компилироваться (`./gradlew build`) и работать как в полной связке со всеми модами из `libs/`, так и в минимальном окружении с Forge и обязательными зависимостями (UniMixins, GTNHLib, lwjgl3ify, VillageNames). Любые интеграции со сторонними опциональными модами изолируются через проверки загрузки мода (`Loader.isModLoaded`) или late-mixins.

---

## ⚡ Обязательные правила для агентов (Agent Rules)

1. **Принятие решений и спорные моменты:**
   - Если в ходе проектирования или реализации возникает неоднозначность, спорный архитектурный выбор или выбор между разными вариантами механики (например, баланс таблиц торговли, механика рабочих станций или конфликт с профессиями VillageNames) — **уведомить пользователя и обсудить решение**.
   - Все остальные типовые задачи, реализацию согласованных фичей, фиксы и тесты выполнять полностью автономно без лишних вопросов.

2. **Тестирование и запуск клиента (Client Testing Workflow):**
   - По окончании работы над задачами **всегда предлагать пользователю запустить тестовый клиент разработчика** (`./gradlew runClient` или `./gradlew runClient25`).
   - Параллельно отслеживать логи в консоли.
   - В случае краша/ошибки немедленно переходить к автономному анализу и устранению проблемы.
   - При штатном закрытии клиента спросить пользователя, все ли прошло хорошо или нужны правки. Если все хорошо — задача считается выполненной.

3. **Повышение версии (SemVer):**
   - При команде на пуш новой версии мода обновлять версию по схеме `MAJOR.MINOR.PATCH`:
     - `MAJOR` — крупные изменения с нарушением обратной совместимости или переработкой структуры сохранений;
     - `MINOR` — добавление новых фичей, профессий, интеграций;
     - `PATCH` — мелкие багфиксы, оптимизации и исправления GUI.

4. **Оформление коммитов (Conventional Commits):**
   - Строгий формат: `<type>(<scope>): <description>` (на английском языке).
   - Примеры:
     - `feat(trades): implement tiered villager leveling and xp progress`
     - `fix(gui): fix trade offer list scrolling with lwjgl3ify`
     - `refactor(compat): isolate VillageNames trade hook reflection`
     - `docs(readme): add dependency download links from GTNH repos`

5. **Краткость и емкость отчетов:**
   - После выполнения задачи давать краткий, содержательный отчет о сделанном (без лишней воды и длинных пересказов).

6. **Проверка перед началом работы:**
   - Перед началом работы всегда проверять скиллы в `.agents/skills/` и правила в `AGENTS.md`.

7. **Синхронизация документации (Documentation Maintenance):**
   - По ходу разработки и реализации новых механик, добавлении параметров конфигурации или изменении совместимости — **обязательно актуализировать `README.md`**, `AGENTS.md` и сопутствующую документацию.

---

## 🔧 Полезные команды сборщика

- Сборка и компиляция: `./gradlew build`
- Форматирование кода: `./gradlew spotlessApply`
- Запуск тестового клиента: `./gradlew runClient`
- Очистка проекта: `./gradlew clean`
