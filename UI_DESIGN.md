# AppLock NFC - UI Design Documentation

A modern, minimalist design following Material Design 3 principles with clean lines, subtle shadows, and elegant animations.

## Color Scheme

### Light Theme
- **Primary**: Purple/Violet (#6750A4)
- **Primary Container**: Light Purple (#E9DDFF)
- **Background**: Off-white (#FEFBFF)
- **Surface**: White cards with subtle elevation
- **Locked State**: Red (#E53935)
- **Unlocked State**: Green (#43A047)

### Dark Theme
- **Primary**: Light Purple (#CFBCFF)
- **Primary Container**: Dark Purple (#4F378A)
- **Background**: Dark Gray (#1C1B1E)
- **Surface**: Elevated dark cards
- Material You dynamic colors supported (Android 12+)

---

## Screen 1: Home Screen

```
┌─────────────────────────────────────────┐
│         ☰ AppLock NFC                   │
│                                          │
│    ┌────────────────────────────┐       │
│    │                            │       │
│    │      ╭──────────╮          │       │
│    │      │    🔓    │          │       │
│    │      │          │          │       │
│    │      │ UNLOCKED │          │       │
│    │      ╰──────────╯          │       │
│    │                            │       │
│    │    3 apps selected         │       │
│    └────────────────────────────┘       │
│                                          │
│    Select apps to lock                   │
│                                          │
│   ╭────────────────────────────────╮    │
│   │ 📱  Facebook             ☐     │    │
│   ╰────────────────────────────────╯    │
│                                          │
│   ╭────────────────────────────────╮    │
│   │ 📷  Instagram            ☑     │    │
│   ╰────────────────────────────────╯    │
│                                          │
│   ╭────────────────────────────────╮    │
│   │ 🎮  TikTok               ☑     │    │
│   ╰────────────────────────────────╯    │
│                                          │
│   ╭────────────────────────────────╮    │
│   │ 📺  YouTube              ☑     │    │
│   ╰────────────────────────────────╯    │
│                                          │
│                                          │
├─────────────────────────────────────────┤
│   🏠 Home    📊 Stats    ⚙️ Settings    │
└─────────────────────────────────────────┘
```

### Features:
1. **Virtual NFC Toggle Button** (Center Top)
   - Large circular button (160dp diameter)
   - Shows lock icon (🔓 unlocked / 🔒 locked)
   - Color changes:
     - Green when unlocked
     - Red when locked
     - Gray when no apps selected
   - Displays countdown timer when locked (e.g., "18:45")
   - Smooth color animation on tap

2. **App List**
   - Alphabetically sorted
   - Each card shows:
     - App icon placeholder (circular)
     - App name (bold)
     - Package name (small, gray)
     - Checkbox (Material 3 style)
   - Cards have subtle elevation (2dp)
   - 8dp spacing between cards
   - Smooth checkbox animation

3. **Header**
   - App title centered
   - Clean, minimal design
   - Slight shadow for depth

---

## Screen 2: Lock Overlay (When Locked App is Opened)

```
┌─────────────────────────────────────────┐
│                                          │
│                                          │
│            ╔═══════════╗                 │
│            ║           ║                 │
│            ║    🔒     ║                 │
│            ║ (pulsing) ║                 │
│            ║           ║                 │
│            ╚═══════════╝                 │
│                                          │
│         Instagram is Locked              │
│                                          │
│   ╭────────────────────────────────╮    │
│   │  📡  Tap your NFC tag or use   │    │
│   │      the app to unlock         │    │
│   ╰────────────────────────────────╯    │
│                                          │
│   ┌──────────────────────────────┐      │
│   │  Enter PIN                   │      │
│   │  ••••                        │      │
│   └──────────────────────────────┘      │
│                                          │
│   ┌──────────────────────────────┐      │
│   │         Unlock               │      │
│   └──────────────────────────────┘      │
│                                          │
│           Go Back                        │
│                                          │
│                                          │
└─────────────────────────────────────────┘
```

### Features:
1. **Full-Screen Overlay**
   - Semi-transparent black background (95% opacity)
   - Cannot be dismissed by back button
   - Prevents app access

2. **Lock Icon**
   - Large lock icon (120dp)
   - Pulsing animation (scales 1.0 → 1.1)
   - White color
   - Eye-catching

3. **App Name Display**
   - Shows which app is locked
   - Large, bold text
   - White color

4. **NFC Instructions Card**
   - Translucent white card
   - NFC icon
   - Clear instructions

5. **PIN Entry** (if enabled)
   - Password-masked input
   - Clean, outlined text field
   - White on dark background
   - Error state shows in red

6. **Unlock Button**
   - Full-width button
   - White background, black text
   - Rounded corners (12dp)
   - 56dp height

7. **Go Back Button**
   - Text button
   - Returns to home screen

---

## Screen 3: Statistics

```
┌─────────────────────────────────────────┐
│         Statistics                       │
│                                          │
│   ╭──────────────╮  ╭──────────────╮    │
│   │  🚫          │  │  📈          │    │
│   │              │  │              │    │
│   │     45       │  │     127      │    │
│   │  Blocked     │  │  Events      │    │
│   ╰──────────────╯  ╰──────────────╯    │
│                                          │
│   Recent Events                          │
│                                          │
│   ╭────────────────────────────────╮    │
│   │ 🔒  Lock enabled               │    │
│   │     System                     │    │
│   │     Dec 14, 15:23:12          │    │
│   ╰────────────────────────────────╯    │
│                                          │
│   ╭────────────────────────────────╮    │
│   │ 🚫  App blocked                │    │
│   │     Instagram                  │    │
│   │     Dec 14, 15:25:45          │    │
│   ╰────────────────────────────────╯    │
│                                          │
│   ╭────────────────────────────────╮    │
│   │ 🔑  Manual unlock              │    │
│   │     Instagram                  │    │
│   │     Dec 14, 15:26:10          │    │
│   ╰────────────────────────────────╯    │
│                                          │
├─────────────────────────────────────────┤
│   🏠 Home    📊 Stats    ⚙️ Settings    │
└─────────────────────────────────────────┘
```

### Features:
1. **Summary Cards** (Top)
   - Two cards side-by-side
   - Primary container color background
   - Large numbers (headline style)
   - Icons for visual interest
   - Shows:
     - Total blocked attempts
     - Total events logged

2. **Recent Events List**
   - Chronological list (newest first)
   - Each event card shows:
     - Event type icon (colored)
     - Event name
     - App name (if applicable)
     - Timestamp
   - Color-coded by event type:
     - Red: Lock enabled, App blocked
     - Blue: Manual/NFC unlock
     - Purple: Auto unlock

3. **Empty State** (when no stats)
   - Large icon
   - "No statistics yet" message
   - Helpful hint text

---

## Screen 4: Settings

```
┌─────────────────────────────────────────┐
│         Settings                         │
│                                          │
│   ╭────────────────────────────────╮    │
│   │  ♿  Accessibility Service      │    │
│   │                                │    │
│   │  Required for app locking to   │    │
│   │  work. Enable AppLock NFC in   │    │
│   │  Accessibility settings.       │    │
│   │                                │    │
│   │  ┌──────────────────────────┐  │    │
│   │  │ Open Accessibility        │  │    │
│   │  │ Settings                  │  │    │
│   │  └──────────────────────────┘  │    │
│   ╰────────────────────────────────╯    │
│                                          │
│   ╭────────────────────────────────╮    │
│   │ ⏱️  Auto-Unlock Duration       │    │
│   │     20 minutes                 │    │
│   ╰────────────────────────────────╯    │
│                                          │
│   ╭────────────────────────────────╮    │
│   │ 🔐  Set PIN                    │    │
│   │     Add extra security         │    │
│   ╰────────────────────────────────╯    │
│                                          │
│                                          │
│                                          │
│                                          │
├─────────────────────────────────────────┤
│   🏠 Home    📊 Stats    ⚙️ Settings    │
└─────────────────────────────────────────┘
```

### Features:
1. **Accessibility Service Card**
   - Prominent placement (top)
   - Primary container color (to draw attention)
   - Clear instructions
   - Direct button to open system settings
   - Icon: Accessibility symbol

2. **Settings Items**
   - Card-based design
   - Each card has:
     - Icon (left)
     - Title (bold)
     - Current value/description (gray)
     - Tap to open dialog
   - Icons are colored with primary color

3. **Dialogs**

   **PIN Setup Dialog:**
   ```
   ╭───────────────────────────╮
   │  Set PIN                  │
   │                           │
   │  Enter PIN (4-6 digits)   │
   │  ┌─────────────────────┐  │
   │  │ •••••               │  │
   │  └─────────────────────┘  │
   │                           │
   │  Confirm PIN              │
   │  ┌─────────────────────┐  │
   │  │ •••••               │  │
   │  └─────────────────────┘  │
   │                           │
   │      Cancel    Set PIN    │
   ╰───────────────────────────╯
   ```

   **Duration Dialog:**
   ```
   ╭───────────────────────────╮
   │  Auto-Unlock Duration     │
   │                           │
   │  Select how long apps     │
   │  should remain locked     │
   │                           │
   │  ○  5 minutes             │
   │  ○  10 minutes            │
   │  ○  15 minutes            │
   │  ●  20 minutes            │
   │  ○  30 minutes            │
   │  ○  45 minutes            │
   │  ○  60 minutes            │
   │                           │
   │  Cancel    Set Duration   │
   ╰───────────────────────────╯
   ```

---

## Bottom Navigation Bar

```
┌─────────────────────────────────────────┐
│  🏠        📊        ⚙️                  │
│  Home     Stats     Settings             │
└─────────────────────────────────────────┘
```

### Design:
- Material 3 NavigationBar
- Three items evenly spaced
- Icons:
  - Filled when selected
  - Outlined when unselected
- Selected item has:
  - Primary container color indicator pill
  - Darker icon color
  - Label in primary color
- Smooth transition animations
- 8dp elevation

---

## Design Principles Applied

### 1. **Minimalism**
- Clean white space
- No clutter or unnecessary elements
- Focus on essential information
- Simple, clear typography

### 2. **Material Design 3**
- Rounded corners (8-16dp)
- Subtle elevations (2-12dp)
- Dynamic color system support
- Proper spacing (4dp grid)
- Material You integration

### 3. **Visual Hierarchy**
- Large, bold headings
- Clear section separation
- Color-coded states
- Consistent iconography
- Proper text sizing

### 4. **Accessibility**
- High contrast ratios
- Large touch targets (48dp minimum)
- Clear labels
- Screen reader support
- Keyboard navigation

### 5. **Animations**
- Smooth transitions (300ms)
- Spring animations for natural feel
- Pulsing lock icon
- Color state changes
- Scale effects on interactions

### 6. **Color Psychology**
- Red = Locked/Danger/Blocked
- Green = Unlocked/Safe/Available
- Purple = Primary actions
- Gray = Disabled/Inactive

---

## Typography Scale

- **Display**: 57sp - Not used
- **Headline Large**: 32sp - Screen titles
- **Headline Medium**: 28sp - "AppLock NFC", "App is Locked"
- **Title Large**: 22sp - Card titles
- **Title Medium**: 16sp - Section headers
- **Body Large**: 16sp - App names
- **Body Medium**: 14sp - Descriptions
- **Body Small**: 12sp - Package names, timestamps
- **Label**: Button text

---

## Component Specifications

### Virtual NFC Toggle Button
- Size: 160 x 160 dp (circular)
- Shape: Circle
- Elevation: 8dp (unlocked), 12dp (locked)
- Icon: 48dp
- Text: Title Medium (16sp, bold)
- Colors:
  - Unlocked: #43A047 (green)
  - Locked: #E53935 (red)
  - Disabled: Surface variant
- Animation: 300ms tween for color change

### App List Cards
- Height: Auto (wrap content)
- Padding: 16dp
- Margin: 8dp between cards
- Elevation: 2dp
- Corner radius: 12dp
- Icon size: 40dp (circular)
- Checkbox: Material 3 style

### Statistics Cards
- Min height: 120dp
- Padding: 16dp
- Corner radius: 16dp
- Elevation: 2dp
- Background: Primary container

### Lock Overlay
- Full screen
- Background: Black @ 95% opacity
- Lock icon: 120dp
- Button height: 56dp
- Input height: 56dp
- Padding: 32dp

---

## Interaction Patterns

### 1. **Selecting Apps**
- Tap anywhere on card to toggle
- Checkbox animates
- Immediate visual feedback
- Updates locked apps count

### 2. **Toggle Lock State**
- Tap virtual NFC button
- Button color animates (300ms)
- Icon changes
- Timer starts/stops
- Haptic feedback (vibration)

### 3. **Opening Locked App**
- Accessibility service detects launch
- Overlay appears instantly
- Animation: Fade in (200ms)
- Lock icon pulses

### 4. **Unlocking with PIN**
- Enter PIN
- Tap unlock button
- If correct: Overlay dismisses
- If incorrect: Red error, field shakes

---

## State Indicators

### Lock States:
1. **Unlocked** - Green button, unlock icon
2. **Locked** - Red button, lock icon, timer countdown
3. **Disabled** - Gray button (no apps selected)

### App States:
1. **Not Locked** - Unchecked box, gray icon background
2. **Locked** - Checked box, primary container background

### Accessibility Service:
1. **Enabled** - Not prominently shown
2. **Disabled** - Warning card in settings

---

## Responsive Design

- Supports phones only (for now)
- Adapts to screen sizes 5" - 7"
- Landscape mode supported
- Proper padding on all screen sizes
- Safe area insets respected

---

This design creates a **professional, modern, and highly functional** app that's a pleasure to use!
