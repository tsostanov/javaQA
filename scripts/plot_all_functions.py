#!/usr/bin/env python
from __future__ import annotations

import argparse
import csv
import math
import subprocess
import sys
from pathlib import Path

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt


ROOT = Path(__file__).resolve().parents[1]
JAR_PATH = ROOT / "target" / "tpojava-1.0-SNAPSHOT.jar"
DEFAULT_OUTPUT_DIR = ROOT / "target" / "report-assets"
EPSILON = "1E-8"
TAU = 2.0 * math.pi

MODULE_CONFIG = {
    "cos": {"from": -TAU, "to": TAU, "step": 0.02, "ylim": (-1.2, 1.2)},
    "sin": {"from": -TAU, "to": TAU, "step": 0.02, "ylim": (-1.2, 1.2)},
    "sec": {"from": -TAU, "to": TAU, "step": 0.02, "ylim": (-10.0, 10.0)},
    "csc": {"from": -TAU, "to": TAU, "step": 0.02, "ylim": (-10.0, 10.0)},
    "cot": {"from": -TAU, "to": TAU, "step": 0.02, "ylim": (-10.0, 10.0)},
    "ln": {"from": 0.05, "to": 4.0, "step": 0.01, "ylim": (-3.5, 1.8)},
    "log2": {"from": 0.05, "to": 4.0, "step": 0.01, "ylim": (-5.0, 2.5)},
    "log3": {"from": 0.05, "to": 4.0, "step": 0.01, "ylim": (-3.0, 1.5)},
    "log5": {"from": 0.05, "to": 4.0, "step": 0.01, "ylim": (-2.0, 1.0)},
    "log10": {"from": 0.05, "to": 4.0, "step": 0.01, "ylim": (-1.5, 0.8)},
    "system": {"from": -TAU, "to": 4.0, "step": 0.02, "ylim": (-10.0, 10.0)},
}

PLOT_ORDER = [
    "cos",
    "sin",
    "sec",
    "csc",
    "cot",
    "ln",
    "log2",
    "log3",
    "log5",
    "log10",
    "system",
]


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Generate CSV exports and plots for all mathematical modules."
    )
    parser.add_argument(
        "--output-dir",
        type=Path,
        default=DEFAULT_OUTPUT_DIR,
        help="Directory for generated CSV files and PNG plots.",
    )
    parser.add_argument(
        "--build",
        action="store_true",
        help="Build the jar before plotting with Maven package.",
    )
    parser.add_argument(
        "--maven-settings",
        type=Path,
        default=ROOT / ".mvn" / "settings.xml",
        help="Path to Maven settings.xml.",
    )
    return parser.parse_args()


def run_command(command: list[str]) -> None:
    subprocess.run(command, cwd=ROOT, check=True)


def build_jar(settings_path: Path) -> None:
    run_command(
        [
            "mvn",
            "-s",
            str(settings_path),
            "-DskipTests",
            "package",
        ]
    )


def ensure_jar_exists(settings_path: Path, should_build: bool) -> None:
    if should_build:
        build_jar(settings_path)
    if not JAR_PATH.exists():
        raise FileNotFoundError(
            f"Jar not found: {JAR_PATH}. Run 'mvn -s {settings_path} package' first "
            "or pass --build."
        )


def export_csv(module_name: str, module_config: dict[str, float], output_file: Path) -> None:
    run_command(
        [
            "java",
            "-jar",
            str(JAR_PATH),
            module_name,
            str(module_config["from"]),
            str(module_config["to"]),
            str(module_config["step"]),
            str(output_file),
            EPSILON,
        ]
    )


def read_csv(csv_path: Path) -> tuple[list[float], list[float]]:
    xs: list[float] = []
    ys: list[float] = []
    with csv_path.open(newline="", encoding="utf-8") as file:
        reader = csv.DictReader(file)
        for row in reader:
            xs.append(float(row["x"]))
            ys.append(float(row["result"]))
    return xs, ys


def plot_single(module_name: str, xs: list[float], ys: list[float], output_file: Path) -> None:
    fig, ax = plt.subplots(figsize=(8, 4.5))
    ax.plot(xs, ys, linewidth=1.4, color="black")
    ax.axhline(0.0, color="gray", linewidth=0.8)
    ax.axvline(0.0, color="gray", linewidth=0.8)
    ax.grid(True, linestyle=":", linewidth=0.6)
    ax.set_title(module_name)
    ax.set_xlabel("x")
    ax.set_ylabel("y")
    ax.set_ylim(MODULE_CONFIG[module_name]["ylim"])
    fig.tight_layout()
    fig.savefig(output_file, dpi=180)
    plt.close(fig)


def plot_overview(all_series: dict[str, tuple[list[float], list[float]]], output_file: Path) -> None:
    fig, axes = plt.subplots(4, 3, figsize=(14, 14))
    axes_flat = list(axes.flatten())

    for index, module_name in enumerate(PLOT_ORDER):
        ax = axes_flat[index]
        xs, ys = all_series[module_name]
        ax.plot(xs, ys, linewidth=1.1, color="black")
        ax.axhline(0.0, color="gray", linewidth=0.7)
        ax.axvline(0.0, color="gray", linewidth=0.7)
        ax.grid(True, linestyle=":", linewidth=0.5)
        ax.set_title(module_name)
        ax.set_ylim(MODULE_CONFIG[module_name]["ylim"])

    for index in range(len(PLOT_ORDER), len(axes_flat)):
        axes_flat[index].axis("off")

    fig.tight_layout()
    fig.savefig(output_file, dpi=180)
    plt.close(fig)


def main() -> int:
    args = parse_args()
    output_dir = args.output_dir.resolve()
    csv_dir = output_dir / "csv"
    plots_dir = output_dir / "plots"
    csv_dir.mkdir(parents=True, exist_ok=True)
    plots_dir.mkdir(parents=True, exist_ok=True)

    ensure_jar_exists(args.maven_settings.resolve(), args.build)

    all_series: dict[str, tuple[list[float], list[float]]] = {}
    for module_name in PLOT_ORDER:
        csv_path = csv_dir / f"{module_name}.csv"
        export_csv(module_name, MODULE_CONFIG[module_name], csv_path)
        xs, ys = read_csv(csv_path)
        all_series[module_name] = (xs, ys)
        plot_single(module_name, xs, ys, plots_dir / f"{module_name}.png")

    plot_overview(all_series, plots_dir / "all-functions.png")

    print(f"CSV exports: {csv_dir}")
    print(f"Plots: {plots_dir}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
