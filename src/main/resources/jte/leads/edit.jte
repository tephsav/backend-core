@import ru.mentee.power.crm.model.Lead
@param Lead lead

<!DOCTYPE html>
<html>
<head>
    <title>Редактирование лида</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
<div class="container mx-auto p-6">
    <h1 class="text-2xl font-bold mb-4">Редактирование лида</h1>
    <form method="post" action="/leads/${String.valueOf(lead.id())}" class="bg-white p-6 rounded shadow">
        <div>
            <input type="hidden" name="id" value="${String.valueOf(lead.id())}">
        </div>
        <div>
            <label for="email" class="block text-sm font-medium text-gray-700 mb-1">
                Email
            </label>
            <input
                    type="email"
                    id="email"
                    name="email"
                    value="${lead.email()}"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    placeholder="example@company.com"
            />
        </div>
        <div>
            <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">
                Phone
            </label>
            <input
                    type="text"
                    id="phone"
                    name="phone"
                    value="${lead.phone()}"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    placeholder="+0000000"
            />
        </div>
        <div>
            <label for="company" class="block text-sm font-medium text-gray-700 mb-1">
                Company
            </label>
            <input
                    type="text"
                    id="company"
                    name="company"
                    value="${lead.company()}"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                    placeholder="Company"
            />
        </div>
        <div>
            <label for="status" class="block text-sm font-medium text-gray-700 mb-1">
                Status
            </label>
            <select
                    id="status"
                    name="status"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
            >
                <option value="NEW" selected="${lead.status().equals("NEW")}">Новый</option>
                <option value="CONTACTED" selected="${lead.status().equals("CONTACTED")}">Связались</option>
                <option value="QUALIFIED" selected="${lead.status().equals("QUALIFIED")}">Квалифицированный</option>
            </select>
        </div>

        <button
                type="submit"
                class="w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-500"
        >
            Редактировать лида
        </button>

        <a
                href = "/leads"
                class="block text-center text-sm text-gray-600 hover:text-gray-900"
        >
            Отмена
        </a>
    </form>
</div>
</body>
</html>
