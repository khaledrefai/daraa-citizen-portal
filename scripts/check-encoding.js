#!/usr/bin/env node
const fs = require('fs');
const path = require('path');

const ROOT = path.join(__dirname, '..');
const TARGET_DIR = path.join(ROOT, 'src', 'main', 'webapp', 'app');
const EXTENSIONS = new Set(['.ts', '.tsx']);
const FORBIDDEN = [
  { char: '\uFFFD', description: 'replacement character (usually indicates decoding problems)' },
  { char: '\uFEFF', description: 'byte order mark (BOM)' },
];

const offenders = [];

const walk = dir => {
  const entries = fs.readdirSync(dir, { withFileTypes: true });
  entries.forEach(entry => {
    const fullPath = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      walk(fullPath);
    } else if (EXTENSIONS.has(path.extname(entry.name))) {
      checkFile(fullPath);
    }
  });
};

const checkFile = filePath => {
  const content = fs.readFileSync(filePath, 'utf8');
  FORBIDDEN.forEach(({ char, description }) => {
    const occurrences = content.split(char).length - 1;
    if (occurrences > 0) {
      offenders.push({ filePath, char, occurrences, description });
    }
  });
};

walk(TARGET_DIR);

if (offenders.length > 0) {
  console.error('Encoding issues detected:');
  offenders.forEach(({ filePath, char, occurrences, description }) => {
    console.error(`- ${filePath}: ${occurrences} occurrence(s) of ${char} (${description})`);
  });
  process.exit(1);
}

console.log('No encoding issues found in application TypeScript files.');
