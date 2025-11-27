#!/bin/sh

# Запускаем Ollama в фоне
echo "Запуск Ollama сервера..."
/bin/ollama serve &
OLLAMA_PID=$!

# Функция для загрузки модели
pull_model() {
    echo "Проверка модели: $1"
    until ollama list | grep -q "$1"; do
        echo "Загрузка модели: $1"
        if ollama pull "$1"; then
            echo "Модель $1 успешно загружена"
            break
        else
            echo "Ошибка загрузки $1, повтор через 10 секунд..."
            sleep 10
        fi
    done
}

# Ждем запуска сервера
echo "Ожидание запуска Ollama..."
until curl -s http://ollama:11434/api/tags > /dev/null; do
    sleep 2
done

echo "Ollama запущен! Начинаем загрузку моделей..."

MODELS="gemma3:270m"

for model in $MODELS; do
    echo "Проверка модели: $model"
    
    # Проверяем, есть ли модель уже загруженной
    if curl -s http://ollama:11434/api/tags | grep -q "\"name\":\"$model\""; then
        echo "✅ Модель $model уже загружена"
    else
        echo "📥 Загрузка модели: $model"
        curl -X POST http://ollama:11434/api/pull -d "{\"name\": \"$model\"}"
        echo "✅ Модель $model загружена"
    fi
done

echo "Все модели загружены! Ollama готов к работе."

# Ждем завершения основного процесса
wait $OLLAMA_PID