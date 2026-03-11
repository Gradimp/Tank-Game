# Tank 2025 🎮

A 2D tank battle game built with JavaFX as part of a BBM103(OOP) GUI programming assignment. The entire game — UI, game loop, collision detection, AI, and camera system — is implemented from scratch in pure Java 8 with no external libraries.

---

## Gameplay

You control a tank navigating a walled map while fending off waves of enemy tanks that spawn from the upper part of the map. Survive as long as possible, rack up kills, and don't let them wear down your three lives.

### Controls

| Key | Action |
|---|---|
| Arrow Keys | Move tank |
| X | Fire bullet |
| P | Pause / unpause |
| R | Restart (game over screen) |
| Escape | Quit |

---

## Features

- **Player tank** with 4-directional movement and tread animation
- **Enemy AI** — randomized direction changes and periodic firing
- **Bullet physics** — constant-speed projectiles with wall and tank collision
- **Explosion animations** on bullet impact and tank destruction
- **Dynamic enemy spawning** at randomized intervals
- **Score tracking** displayed in the top-left corner
- **3-life system** with respawning at the original start position
- **Pause menu** (P key) with restart and quit options
- **Game over screen** showing final score, with restart (R) and quit (Escape)
- **Camera tracking** — the viewport follows the player across the full map
- **Scrolling map** — larger-than-screen world with horizontal and vertical scrolling
- **Indestructible walls** forming the map boundary and interior obstacles

---

## Requirements

- Java 8 (Oracle JDK) (Because of their bundling of JavaFX, 8u441 or earlier should work, but 8u431 is recommended)

---

## Running the Project

```bash
javac Main.java
java Main
```

Or open in IntelliJ IDEA with the `src` folder marked as Sources Root and SDK set to Java 8.

---

## Project Structure

```
Tank2025/
├── src/          # All Java source files
├── assets/       # Sprites
├── .gitignore
└── README.md
```

---

## Notes

- No external libraries, FXML, SceneBuilder, or CSS used
- All GUI built programmatically via JavaFX API
- JavaDoc-style comments throughout the source
