# Cherlsonia Utils Plugin

## Commands Overview

This plugin provides a set of useful commands for both players and staff. Below is a list of all available commands and a short description of what each does.

### Player Commands

- **/spawn** — Teleports you to the spawn location set in the config.
- **/tpa <player>** — Sends a teleport request to another player.
- **/tpaccept** — Accepts a pending teleport request.
- **/tpadeny** — Denies a pending teleport request.
- **/help** — Shows a help menu with server commands (customizable).
- **/rtpa** — Teleports you to a random location in the world (has a cooldown).
- **/rules** — Displays the server rules (customizable).

### Staff Commands (OP or with the permission tag set in config)

- **/feed** — Restores your hunger bar.
- **/heal** — Restores your health and extinguishes fire.
- **/clearchat** — Clears the chat for all players and shows a message.
- **/admintp <player>** — Teleports you to any player.
- **/mute <player>** — Prevents a player from chatting (mute).
- **/unmute <player>** — Allows a muted player to chat again (unmute).

---

## Configuration (config.yml)

When you first run the plugin, a `config.yml` file is generated in the plugin's folder. You can edit the following options:

- **messages.prefix** — The prefix for all plugin messages.
- **messages.player_only** — Message shown if a command is used by the console.
- **messages.operator_only** — Message shown if a command requires staff permissions.
- **messages.invalid_item** — Message for invalid item usage.
- **messages.invalid_name** — Message for invalid warp/waypoint names.
- **spawn** — The world and coordinates for the `/spawn` command.
- **help_prompt** — The text shown by `/help`. You can customize this list.
- **rtpa_cooldown** — Cooldown in seconds for the `/rtpa` command.
- **clearchat_message** — Message shown after using `/clearchat`.
- **admin_permission_tag** — The scoreboard tag required for staff commands (default: "mod").
- **rules** — The text shown by `/rules`. You can edit the server rules here.

**To customize the plugin, simply open `config.yml` and edit the values as you wish.**

---

For any issues or questions, check your config file or contact your server staff.