# 📦 Cherlsonia Utils Plugin

## 📝 Descriere

**Cherlsonia Utils** este un plugin utilitar pentru servere Minecraft, oferind comenzi esențiale pentru jucători și staff: teleportare, gestionare chat, reguli, mute/unmute, feed/heal, și multe altele. Este ușor de configurat și personalizat.

---

## ⚡ Instalare

1. **Descarcă** fișierul JAR al pluginului.
2. **Copiază** fișierul în folderul `plugins/` al serverului tău Minecraft.
3. **Repornește** serverul pentru a genera fișierele de configurare.
4. **Configurează** pluginul editând `config.yml` după preferințe.
5. **Folosește** comenzile disponibile pe server!

---

## ⚙️ Configurare (`config.yml`)

Exemplu de fișier `config.yml` generat automat:

```yaml
# Cherlsonia Utils Plugin Configuration

messages:
  prefix: "&8[&bCherlsonia&8] &r"
  player_only: "&cOnly players can use this command."
  operator_only: "&cOnly operators can use this command."
  invalid_item: "&cInvalid item: %item%"
  invalid_name: "&cInvalid warp name! Use only letters, numbers, and underscores (max 32 characters)."

spawn:
  world: "world"
  x: 0.5
  y: 64.0
  z: 0.5
  yaw: 0.0
  pitch: 0.0

help_prompt: |
  /help opens this menu
  /spawn teleports you to the center of the world
  /menu opens the main menu
  /faction lets you make and manage your faction
  /claim allows you to claim land
  /w lets you send private messages
  /waypoint lets you teleport to major locations
  /tpa allows you to tpa to someone
  /ptpa allows you to set a partner to tp to
  /rtpa teleports you randomly in the world (5 min cooldown)
  /tpaccept accept a tpa request
  /tpadeny deny a tpa request
  /investor lets investors claim rewards
  /donator lets donators claim rewards
  /home lets donators set a home
  /auction opens auction menu

rtpa_cooldown: 300

clearchat_message: "&aChat has been cleared by an admin!"

admin_permission_tag: "mod"

rules: |
  1. Be respectful to all players.
  2. No cheating or exploiting bugs.
  3. No griefing or stealing.
  4. No spamming or advertising.
  5. Follow staff instructions at all times.
```

**Ce poți personaliza:**
- Prefixul și mesajele afișate de plugin.
- Coordonatele de spawn.
- Promptul de ajutor afișat la `/help`.
- Cooldown-ul pentru `/rtpa`.
- Mesajul de clear chat.
- Tagul de permisiune pentru staff.
- Regulile serverului afișate la `/rules`.

---

## 🛡️ Permisiuni

- **Comenzi de staff** (ex: `/feed`, `/heal`, `/clearchat`, `/admintp`, `/mute`, `/unmute`) necesită ca jucătorul să fie OP sau să aibă tag-ul definit la `admin_permission_tag` (implicit: `mod`).
- **Comenzi de bază** (ex: `/spawn`, `/tpa`, `/tpaccept`, `/tpadeny`, `/help`, `/rtpa`, `/rules`) pot fi folosite de toți jucătorii.

---

## 🕹️ Comenzi principale

| Comandă         | Descriere                                                                 | Cine poate folosi         |
|-----------------|---------------------------------------------------------------------------|---------------------------|
| `/spawn`        | Teleportează jucătorul la coordonatele de spawn din config                | Toți jucătorii            |
| `/tpa <player>` | Trimite cerere de teleportare către alt jucător                           | Toți jucătorii            |
| `/tpaccept`     | Acceptă o cerere de teleportare                                           | Toți jucătorii            |
| `/tpadeny`      | Refuză o cerere de teleportare                                            | Toți jucătorii            |
| `/help`         | Afișează promptul de ajutor configurat                                    | Toți jucătorii            |
| `/rtpa`         | Teleportează jucătorul la o locație random (cu cooldown)                  | Toți jucătorii            |
| `/feed`         | Umple bara de foame                                                       | Staff (OP sau `mod`)      |
| `/heal`         | Umple viața și stinge focul                                               | Staff (OP sau `mod`)      |
| `/clearchat`    | Curăță chatul pentru toți jucătorii                                       | Staff (OP sau `mod`)      |
| `/rules`        | Afișează regulile serverului                                              | Toți jucătorii            |
| `/admintp <p>`  | Teleportează staff-ul la orice jucător                                    | Staff (OP sau `mod`)      |
| `/mute <p>`     | Oprește chatul pentru un jucător                                          | Staff (OP sau `mod`)      |
| `/unmute <p>`   | Permite din nou chatul pentru un jucător                                  | Staff (OP sau `mod`)      |

---

## 🛠️ Alte detalii

- **Cooldown-ul** pentru `/rtpa` se setează în secunde în `config.yml` (`rtpa_cooldown`).
- **Regulile** și **promptul de help** pot fi editate direct în config, suportă color codes Minecraft (&a, &c, etc).
- **Tagul de staff** poate fi schimbat (`admin_permission_tag`), util dacă vrei să folosești alt tag decât `mod`.

---

## ❓ Întrebări frecvente

- **Nu funcționează o comandă?**  
  Verifică permisiunile, tag-urile și sintaxa din `config.yml`.
- **Vrei să schimbi mesajele?**  
  Editează secțiunea `messages` din config.
- **Vrei să schimbi coordonatele de spawn?**  
  Modifică secțiunea `spawn` din config.

---

## 🧑‍💻 Suport

Pentru bug-uri sau întrebări, deschide un issue pe GitHub sau contactează staff-ul serverului tău.

---

Dacă vrei să personalizezi și mai mult pluginul, consultă fișierele de configurare și codul sursă pentru opțiuni avansate!